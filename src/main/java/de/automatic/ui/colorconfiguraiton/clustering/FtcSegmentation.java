package de.automatic.ui.colorconfiguraiton.clustering;

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

public class FtcSegmentation {

	public ClusterContainer segment(SampleList samples) {
		Histogram histo = ConversionService.toHistogram(samples, Channels.C1, 64, true);
		Segmentation seg = findMinima(histo);

		// histo = createSampleData();
		visualizeSegmentation(histo, seg, 0);

		// T-Test
		Random r = new Random();
		for (int i = 0; i < 100; i++) {
			int j = r.nextInt(seg.size());
			if (testUnimodalHypthesisFor(j, histo, seg)) {
				seg.remove(j);
			}
		}
		// System.out.println();
		visualizeSegmentation(histo, seg, 300);
		ClusterContainer clusters = new ClusterContainer();
		for (int i = 0; i < seg.size() - 2; i++) {
			if (i == 0) {
				SampleList modeSamples = new SampleList();
				for (int j = seg.get(i); j < seg.get(i + 1); j++) {
					for (Sample s : histo.get(j).getSamples()) {
						modeSamples.add(s);
					}
				}
				for (int j = seg.get(seg.size() - 2); j < seg.get(seg.size() - 1); j++) {
					for (Sample s : histo.get(j).getSamples()) {
						modeSamples.add(s);
					}
				}
				clusters.add(new Cluster(modeSamples, CalculationService.calculateMean(modeSamples)));

			} else {
				SampleList modeSamples = new SampleList();
				for (int j = seg.get(i); j < seg.get(i + 1); j++) {
					for (Sample s : histo.get(j).getSamples()) {
						modeSamples.add(s);
					}
				}
				clusters.add(new Cluster(modeSamples, CalculationService.calculateMean(modeSamples)));
			}
		}
		return clusters;
	}

	private boolean testUnimodalHypthesisFor(int index, Histogram histo, Segmentation seg) {

		if (index == 0 || index == seg.size() - 1) {
			return false;
		}
		int start = seg.get(index - 1);
		int end = seg.get(index + 1);
		// start = 7;
		// end = 16;
		// System.out.println(start);
		// System.out.println(end);

		boolean confirmed = false;

		for (int i = start + 1; i <= end - 1; i++) {
			Histogram incHisto = getGrenanderEstimator("inc", histo, start, i);
			Histogram decHisto = getGrenanderEstimator("dec", histo, i, end);

			if (similiar(histo, incHisto, start, i) && similiar(histo, decHisto, i, end)) {
				confirmed = true;
			}
		}

		return confirmed;
	}

	private Histogram getGrenanderEstimator(String direction, Histogram histo, int start, int end) {
		Histogram histo2 = direction.equals("dec") ? decreasingGrenanderEstimator(histo, start, end)
				: increasingGrenanderEstimator(histo, start, end);

		for (int i = 0; i < 10; i++) {
			histo2 = direction.equals("dec") ? decreasingGrenanderEstimator(histo2, start, end)
					: increasingGrenanderEstimator(histo2, start, end);
		}
		return histo2;
	}

	private boolean similiar(Histogram histo1, Histogram histo2, int start, int end) {
		SummaryStatistics stats = new SummaryStatistics();
		for (int i = start; i <= end; i++) {
			double valueToAdd = Math.abs(histo1.get(i).getValue() - histo2.get(i).getValue());
			stats.addValue(valueToAdd);
		}
		if (stats.getMean() == 0 || stats.getStandardDeviation() == 0) {
			// System.out.println("0 Case");
			return true;
		}
		double t = Math.sqrt(stats.getN()) * stats.getMean() / stats.getStandardDeviation();
		double criticalT = new TDistribution(end - start).inverseCumulativeProbability(0.97);
		return t < criticalT;
	}

	public Histogram decreasingGrenanderEstimator(Histogram histo, int start, int end) {
		Histogram gHisto = new Histogram(histo);

		for (int i = start; i < end; i++) {
			if (i + 1 < gHisto.getBins() && gHisto.get(i + 1).getValue() - gHisto.get(i).getValue() > 0.0) {
				// look ahead
				int j = i;
				double mean = gHisto.get(i).getValue();
				do {
					j++;
					mean += gHisto.get(j).getValue();
				} while (j + 1 <= end && gHisto.get(j + 1).getValue() - gHisto.get(j).getValue() > 0.0);

				mean /= ((double) j - (double) i + 1.0);
				for (int k = i; k <= j; k++) {
					gHisto.set(k, new HistogramElement(histo.get(k).getKey(), mean));
				}
				i = j - 1;
			}

		}

		return gHisto;
	}

	public Histogram increasingGrenanderEstimator(Histogram histo, int start, int end) {
		Histogram gHisto = new Histogram(histo);

		for (int i = start; i < end; i++) {
			if (i + 1 < gHisto.getBins() && gHisto.get(i + 1).getValue() - gHisto.get(i).getValue() < 0.0) {
				int j = i;
				double mean = gHisto.get(i).getValue();
				do {
					j++;
					mean += gHisto.get(j).getValue();
				} while (j + 1 <= end && gHisto.get(j + 1).getValue() - gHisto.get(j).getValue() < 0.0);

				mean /= ((double) j - (double) i + 1);
				for (int k = i; k <= j; k++) {
					gHisto.set(k, new HistogramElement(histo.get(k).getKey(), mean));
				}
				i = j - 1;
			}

		}
		return gHisto;
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

	public void visualizeSegmentation(Histogram histogram, Segmentation segmentation, int y) {
		JFrame frame = new JFrame("Segmentation Visualization");
		frame.setLayout(new GridLayout(1, 1));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JFreeChart chart = getChart(histogram);
		XYPlot plot = (XYPlot) chart.getPlot();

		if (segmentation != null) {
			int index = 0;
			for (Integer i : segmentation) {
				ValueMarker marker = new ValueMarker(i);
				marker.setPaint(Color.BLACK);
				marker.setLabel(Integer.toString(index++));
				plot.addDomainMarker(marker);
			}
		}

		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(1000, 270));

		frame.add(chartPanel);
		frame.pack();
		frame.setLocation(0, y);
		frame.setVisible(true);
	}

	private JFreeChart getChart(Histogram histogram) {
		XYSeriesCollection dataset = new XYSeriesCollection();
		XYSeries series = new XYSeries("Series");
		for (int i = 0; i < histogram.getBins(); i++) {
			if (i > 0) {
				series.add(i, histogram.get(i - 1).getValue());
			}
			series.add(i, histogram.get(i).getValue());
		}
		dataset.addSeries(series);
		JFreeChart chart = ChartFactory.createXYLineChart("Segmentation", "C1", "Count", dataset);
		return chart;
	}

	// private JFreeChart getChart(Histogram histogram) {
	// double[] values = new double[(int) histogram.getTotalCount()];
	// HistogramDataset dataset = new HistogramDataset();
	//
	// int i = 0;
	// for (HistogramElement e : histogram) {
	// for (int j = 0; j < (int) e.getValue(); j++) {
	// values[i++] = e.getKey();
	// }
	// }
	//
	// dataset.addSeries("H1", values, histogram.getBins());
	// JFreeChart chart = ChartFactory.createHistogram("Segmentation", "C1",
	// "Count", dataset,
	// PlotOrientation.VERTICAL, false, false, false);
	// return chart;
	// }

	private Histogram createSampleData() {
		Histogram histo = new Histogram(32, Channels.C1);
		histo.add(7);
		histo.add(4);
		histo.add(6);
		histo.add(5);
		histo.add(3);
		histo.add(2);
		histo.add(1);
		histo.add(2);
		histo.add(3);
		histo.add(5);
		histo.add(3);
		histo.add(4);
		histo.add(0);
		histo.add(0);
		histo.add(0);
		histo.add(0);
		histo.add(0);
		histo.add(5);
		histo.add(4);
		histo.add(2);
		histo.add(7);
		histo.add(0);
		histo.add(5);
		histo.add(4);
		histo.add(4);
		histo.add(4);
		histo.add(4);
		histo.add(4);
		histo.add(4);
		histo.add(4);
		histo.add(5);
		histo.add(0);
		histo.normalize();
		return histo;
	}
}
