package de.automatic.ui.colorconfiguraiton.clustering;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;
import org.apache.commons.math3.stat.inference.TTest;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultKeyedValueDataset;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYBarDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import de.automatic.ui.colorconfiguraiton.entities.Channels;
import de.automatic.ui.colorconfiguraiton.entities.Histogram;
import de.automatic.ui.colorconfiguraiton.entities.HistogramElement;
import de.automatic.ui.colorconfiguraiton.entities.SampleList;
import de.automatic.ui.colorconfiguraiton.entities.HsiSample;
import de.automatic.ui.colorconfiguraiton.entities.Sample;
import de.automatic.ui.colorconfiguraiton.entities.Segmentation;
import de.automatic.ui.colorconfiguraiton.services.ConversionService;

public class FtcSegmentation {

	public void segment(SampleList samples) {
		Histogram histo = ConversionService.toHistogram(samples, Channels.C1, 32, true);
		histo = createSampleData();

		// T-Test
		int start = 0;
		int end = 10;
		Histogram histo2 = decreasingGrenanderEstimator(histo, start, end);
		for (int i = 0; i < 10; i++) {
			histo2 = decreasingGrenanderEstimator(histo2, start, end);
		}
		
		SummaryStatistics stats = new SummaryStatistics();
		double error = 0.0;
		for (int i = start; i <= end; i++) {
			double valueToAdd = Math.abs(histo.get(i).getValue() - histo2.get(i).getValue());
			error += valueToAdd;
			System.out.println(valueToAdd);
			stats.addValue(valueToAdd);
			// System.out.println(sample1[i]);
			// System.out.println(sample2[i]);
		}
		System.out.println("--------");
		// System.out.println(new TTest().tTest(sample1, sample2));
		// System.out.println(new TTest().tTest(sample1, sample2, 0.5));
		System.out.println(stats.getMean());
		System.out.println(stats.getStandardDeviation());
		System.out.println(stats.getN());
		System.out.println(Math.sqrt(stats.getN()) * stats.getMean() / stats.getStandardDeviation());
		System.out.println(new TDistribution(end - start).inverseCumulativeProbability(0.95));
		System.out.println("--------");
		System.out.println(error);
		System.out.println(error / Math.sqrt(end - start + 1));

		visualizeSegmentation(histo, findMinima(histo), 0);
		visualizeSegmentation(histo2, findMinima(histo2), 330);

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
		segmentation.add(0);
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

			for (Integer i : segmentation) {
				ValueMarker marker = new ValueMarker(i);
				marker.setPaint(Color.BLACK);
				marker.setLabel("Marker");
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
			if(i > 0) {
				series.add(i, histogram.get(i-1).getValue());
			}
			series.add(i, histogram.get(i).getValue());
		}
		dataset.addSeries(series);
		JFreeChart chart = ChartFactory.createXYLineChart  ("Segmentation", "C1", "Count", dataset);
		return chart;
	}
	
//	private JFreeChart getChart(Histogram histogram) {
//		double[] values = new double[(int) histogram.getTotalCount()];
//		HistogramDataset dataset = new HistogramDataset();
//
//		int i = 0;
//		for (HistogramElement e : histogram) {
//			for (int j = 0; j < (int) e.getValue(); j++) {
//				values[i++] = e.getKey();
//			}
//		}
//
//		dataset.addSeries("H1", values, histogram.getBins());
//		JFreeChart chart = ChartFactory.createHistogram("Segmentation", "C1", "Count", dataset,
//				PlotOrientation.VERTICAL, false, false, false);
//		return chart;
//	}

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
		histo.add(5);
		histo.add(7);
		histo.add(3);
		histo.add(0);
		histo.add(3);
		histo.add(2);
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
