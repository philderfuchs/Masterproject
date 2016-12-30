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
import de.automatic.ui.colorconfiguraiton.vis.OneDimHistogramVisualizer;

public class FtcSegmentation {

	public Segmentation segment(Histogram histo) {
		Segmentation seg = findMinima(histo);

		// histo = SampleDataService.createSampleHistogram();
		new OneDimHistogramVisualizer("All Minima", histo, seg, 0);

		// T-Test
		Random r = new Random();
		for (int i = 0; i < 100; i++) {
			int j = r.nextInt(seg.size());
			if (testUnimodalHypthesisFor(j, histo, seg)) {
				seg.remove(j);
			}
		}
		// System.out.println();
		new OneDimHistogramVisualizer("Reduced Minima", histo, seg, 300);
		
		return seg;
	}

	private boolean testUnimodalHypthesisFor(int index, Histogram histo, Segmentation seg) {

		if (index == 0 || index == seg.size() - 1) {
			return false;
		}
		int start = seg.get(index - 1);
		int end = seg.get(index + 1);

		boolean confirmed = false;
		StatisticalTest tester = new weikerTTest();

		for (int i = start + 1; i <= end - 1; i++) {
			Histogram incHisto = GrenanderEstimator.poolAdjacentViolator("inc", histo, start, i);
			Histogram decHisto = GrenanderEstimator.poolAdjacentViolator("dec", histo, i, end);

			if (tester.similiar(histo, incHisto, start, i) && tester.similiar(histo, decHisto, i, end)) {
				confirmed = true;
			}
		}

		return confirmed;
	}

	private Segmentation findMinima(Histogram histo) {
		Segmentation segmentation = new Segmentation(Channels.C1);
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
