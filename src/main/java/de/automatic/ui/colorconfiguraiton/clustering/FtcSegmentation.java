package de.automatic.ui.colorconfiguraiton.clustering;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.statistics.HistogramDataset;

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
		Histogram histo = ConversionService.toHistogram(samples, Channels.C1, 128);
		histo = createSampleData();
		visualizeSegmentation(histo, findMinima(histo));
	}

	public Histogram decreasingGrenanderEstimator(Histogram histo, int start, int end) {
		Histogram gHisto = new Histogram(histo);

		for (int i = start; i < end; i++) {
			if (i + 1 < gHisto.getBins() && gHisto.get(i + 1).getValue() - gHisto.get(i).getValue() > 0) {
				// look ahead
				int j = i;
				int mean = gHisto.get(i).getValue();
				do {
					j++;
					mean += gHisto.get(j).getValue();
				} while (j + 1 <= end && gHisto.get(j + 1).getValue() - gHisto.get(j).getValue() > 0);

				mean /= (j - i + 1);
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
			if (i + 1 < gHisto.getBins() && gHisto.get(i + 1).getValue() - gHisto.get(i).getValue() < 0) {
				// look ahead
				int j = i;
				int mean = gHisto.get(i).getValue();
				do {
					j++;
					mean += gHisto.get(j).getValue();
				} while (j + 1 <= end && gHisto.get(j + 1).getValue() - gHisto.get(j).getValue() < 0);

				mean /= (j - i + 1);
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
		// System.out.println(segmentation.size());
		return segmentation;
	}

	public void visualizeSegmentation(Histogram histogram, Segmentation segmentation) {
		JFrame frame = new JFrame("Segmentation Visualization");
		frame.setLayout(new GridLayout(2, 1));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JFreeChart chart = getChart(histogram);

		if (segmentation != null) {
			XYPlot plot = (XYPlot) chart.getPlot();

			for (Integer i : segmentation) {
				ValueMarker marker = new ValueMarker(histogram.get(i).getKey());
				marker.setPaint(Color.BLACK);
				marker.setLabel("Marker");
				plot.addDomainMarker(marker);
			}
		}

		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(1000, 270));

		frame.add(chartPanel);
		ChartPanel chartPanel2 = new ChartPanel(getChart(increasingGrenanderEstimator(histogram, 15, 20)));
		chartPanel2.setPreferredSize(new java.awt.Dimension(1000, 270));
		frame.add(chartPanel2);
		frame.pack();
		frame.setVisible(true);
	}

	private JFreeChart getChart(Histogram histogram) {
		double[] values = new double[histogram.getTotalCount()];
		HistogramDataset dataset = new HistogramDataset();

		int i = 0;
		for (HistogramElement e : histogram) {
			for (int j = 0; j < e.getValue(); j++) {
				values[i++] = e.getKey();
			}
		}

		dataset.addSeries("H1", values, histogram.getBins());
		JFreeChart chart = ChartFactory.createHistogram("Segmentation", "C1", "Count", dataset,
				PlotOrientation.VERTICAL, false, false, false);
		return chart;
	}

	private Histogram createSampleData() {
		Histogram histo = new Histogram(32, Channels.C1);
		histo.add(5);
		histo.add(3);
		histo.add(6);
		histo.add(5);
		histo.add(3);
		histo.add(2);
		histo.add(1);
		histo.add(3);
		histo.add(4);
		histo.add(0);
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

		return histo;
	}
}
