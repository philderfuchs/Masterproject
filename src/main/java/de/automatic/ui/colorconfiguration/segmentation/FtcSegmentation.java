package de.automatic.ui.colorconfiguration.segmentation;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Random;

import javax.swing.JFrame;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import de.automatic.ui.colorconfiguraiton.entities.Channels;
import de.automatic.ui.colorconfiguraiton.entities.Cluster;
import de.automatic.ui.colorconfiguraiton.entities.ClusterContainer;
import de.automatic.ui.colorconfiguraiton.entities.Histogram;
import de.automatic.ui.colorconfiguraiton.entities.HistogramElement;
import de.automatic.ui.colorconfiguraiton.entities.Sample;
import de.automatic.ui.colorconfiguraiton.entities.SampleList;
import de.automatic.ui.colorconfiguraiton.entities.Segmentation;
import de.automatic.ui.colorconfiguraiton.services.CalculationService;
import de.automatic.ui.colorconfiguraiton.services.ConversionService;
import de.automatic.ui.colorconfiguraiton.services.SampleDataService;
import de.automatic.ui.colorconfiguraiton.visualisation.OneDimHistogramVisualizer;

public class FtcSegmentation {

	private int windowCount;
	private static final int visWidth = 550;
	private static final int visHeight = 120;
	private static final int windowsPerColumn = 6;

	private static double flatThreshold = 0.05;

	public FtcSegmentation() {
		windowCount = 0;
	}

	public Segmentation segment(Histogram histo, String title) {
		Segmentation seg = findMinima(histo);

		// histo = SampleDataService.createSampleHistogram();
		new OneDimHistogramVisualizer(title + " | All Minima", histo, seg, (windowCount / windowsPerColumn) * visWidth,
				(windowCount % windowsPerColumn) * visHeight, visWidth, visHeight);
		windowCount++;

		// step 2: merge consecutive segments
		Random r = new Random();
		for (int i = 0; i < 100; i++) {

			// stop if only start and stop marker left
			if (seg.size() < 3)
				break;

			int chosenIndex = r.nextInt(seg.size());
			if (testUnimodalHypthesisFor(chosenIndex, histo, seg, 0)) {
				seg.remove(chosenIndex);
			}
		}

		// step 3: merge unions of segments
		for (int i = 0; i < 100; i++) {

			// stop if only start and stop marker left
			if (seg.size() < 3)
				break;
			int chosenIndex = r.nextInt(seg.size());
			for (int unions = 1; unions + chosenIndex < seg.size() - 1; unions++) {
				if (testUnimodalHypthesisFor(chosenIndex, histo, seg, unions)) {
					for (int j = chosenIndex; j <= chosenIndex + unions; j++) {
						seg.remove(j);
						System.out.println("Worked");
					}
					break;
				}
			}
		}

		new OneDimHistogramVisualizer(title + " | Reduced Minima", histo, seg,
				(windowCount / windowsPerColumn) * visWidth, (windowCount % windowsPerColumn) * visHeight, visWidth,
				visHeight);
		windowCount++;
		return seg;
	}

	private boolean testUnimodalHypthesisFor(int index, Histogram histo, Segmentation seg, int unionCount) {

		if (index == 0 || index == seg.size() - 1) {
			return false;
		}
		int start = seg.get(index - 1);
		int end = seg.get(Math.min(index + 1 + unionCount, seg.size() - 1));

		// first look if there are no significant histogram peaks in the given
		// range
//		if (tooFlat(histo, start, index))
//			return true;
//		if (tooFlat(histo, index, end))
//			return true;

		// StatisticalTest tester = new WeikerTTest();
		StatisticalTest tester = new MaxDistanceTest();

		// maybe whole segment is kind of monotone
		if (tester.similiar(histo, GrenanderEstimator.poolAdjacentViolator("inc", histo, start, end), start, end)
				|| tester.similiar(histo, GrenanderEstimator.poolAdjacentViolator("dec", histo, start, end), start,
						end))
			return true;

		for (int i = start + 1; i < end - 1; i++) {
			Histogram incHisto = GrenanderEstimator.poolAdjacentViolator("inc", histo, start, i);
			Histogram decHisto = GrenanderEstimator.poolAdjacentViolator("dec", histo, i, end);

			if (tester.similiar(histo, incHisto, start, i) && tester.similiar(histo, decHisto, i, end)) {
				return true;
			}
		}

		return false;
	}

	private boolean tooFlat(Histogram histo, int start, int end) {
		if (flatThreshold == 0) {
			return false;
		}
		boolean tooFlat = true;
		for (int i = start; i < end; i++) {
			if (histo.get(i).getValue() > flatThreshold) {
				tooFlat = false;
			}
		}
		return tooFlat;
	}

	private Segmentation findMinima(Histogram histo) {
		Segmentation segmentation = new Segmentation(histo.getChannel());
		segmentation.add(0);

		for (int i = 1; i < histo.getBins() - 1; i++) {
			if (histo.get(i).getValue() - histo.get(i - 1).getValue() < 0) {
				if (histo.get(i + 1).getValue() - histo.get(i).getValue() > 0) {
					segmentation.add(i);
				} else if (histo.get(i + 1).getValue() - histo.get(i).getValue() == 0) {
					// maybe plateu
					int j = i + 1;
					while (j < histo.getBins() - 1 && histo.get(j + 1).getValue() - histo.get(j).getValue() == 0) {
						j++;
					}
					if (j < histo.size() - 1 && histo.get(j + 1).getValue() - histo.get(j).getValue() > 0) {
						segmentation.add(i + (j - i) / 2);
						i = j - 1;
					}
				}
			}
		}
		segmentation.add(histo.getBins() - 1);
		return segmentation;
	}

}
