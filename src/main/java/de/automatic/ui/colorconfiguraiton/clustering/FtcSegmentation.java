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

	private static double threshold = 3.0;

	public void segment(SampleList samples) {

		// Segmentation segmentation = new Segmentation(Channels.C1);
		// segmentation.add(30.0);
		// segmentation.add(60.0);
		// segmentation.add(120.0);
		// segmentation.add(240.0);
		// histogram = createSampleData();

		Histogram histo = ConversionService.toHistogram(samples, Channels.C1, 32);
		visualizeSegmentation(histo, findMinima(histo));
	}

	private Segmentation findMinima(Histogram histo) {

		Segmentation segmentation = new Segmentation(Channels.C1);

		for (int i = 1; i < histo.getBins() - 1; i++) {
			if (histo.get(i).getValue() - histo.get(i - 1).getValue() < 0) {
				if (histo.get(i + 1).getValue() - histo.get(i).getValue() > 0) {
					segmentation.add(histo.get(i).getKey());
				} else if (histo.get(i + 1).getValue() - histo.get(i).getValue() == 0) {
					// maybe plateu
					int j = i + 1;
					while (j < histo.getBins() - 1 && histo.get(j + 1).getValue() - histo.get(j).getValue() == 0) {
						j++;
					}
					if (j < histo.size() - 1 && histo.get(j + 1).getValue() - histo.get(j).getValue() > 0) {
						double marker = histo.get(i).getKey()
								+ (histo.get(j).getKey() - histo.get(i).getKey()) / 2.0;
						segmentation.add(marker);
						i = j - 1;
					}
				}
			}
		}
		return segmentation;

	}

	public void visualizeSegmentation(Histogram histogram, Segmentation segmentation) {
		JFrame frame = new JFrame("Segmentation Visualization");
		frame.setLayout(new GridLayout(2, 1));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JFreeChart chart = getChart(histogram);

		if (segmentation != null) {
			XYPlot plot = (XYPlot) chart.getPlot();

			for (Double d : segmentation) {
				ValueMarker marker = new ValueMarker(d);
				marker.setPaint(Color.BLACK);
				marker.setLabel("Marker");
				plot.addDomainMarker(marker);
			}
		}

		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(1000, 270));

		frame.add(chartPanel);
		ChartPanel chartPanel2 = new ChartPanel(getChart(histogram));
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

	private SampleList createSampleData() {
		SampleList histo = new SampleList();
		histo.add(new HsiSample(4.0, 0.0, 0.0, 2));
		histo.add(new HsiSample(5.0, 0.0, 0.0, 3));
		histo.add(new HsiSample(6.0, 0.0, 0.0, 5));
		histo.add(new HsiSample(7.0, 0.0, 0.0, 4));
		histo.add(new HsiSample(8.0, 0.0, 0.0, 6));
		histo.add(new HsiSample(9.0, 0.0, 0.0, 1));

		histo.add(new HsiSample(12.0, 0.0, 0.0, 1));
		histo.add(new HsiSample(13.0, 0.0, 0.0, 3));
		histo.add(new HsiSample(14.0, 0.0, 0.0, 1));

		histo.add(new HsiSample(14.5, 0.0, 0.0, 3));
		histo.add(new HsiSample(21.0, 0.0, 0.0, 5));
		histo.add(new HsiSample(22.0, 0.0, 0.0, 7));
		histo.add(new HsiSample(23.0, 0.0, 0.0, 3));
		histo.add(new HsiSample(24.0, 0.0, 0.0, 4));
		histo.add(new HsiSample(25.0, 0.0, 0.0, 3));
		histo.add(new HsiSample(26.0, 0.0, 0.0, 6));
		histo.add(new HsiSample(27.0, 0.0, 0.0, 5));
		histo.add(new HsiSample(28.0, 0.0, 0.0, 7));
		histo.add(new HsiSample(29.0, 0.0, 0.0, 6));
		histo.add(new HsiSample(30.0, 0.0, 0.0, 4));

		histo.add(new HsiSample(34.0, 0.0, 0.0, 5));
		histo.add(new HsiSample(35.0, 0.0, 0.0, 4));
		histo.add(new HsiSample(36.0, 0.0, 0.0, 4));
		histo.add(new HsiSample(37.0, 0.0, 0.0, 4));
		histo.add(new HsiSample(38.0, 0.0, 0.0, 4));
		histo.add(new HsiSample(39.0, 0.0, 0.0, 4));
		histo.add(new HsiSample(40.0, 0.0, 0.0, 4));
		histo.add(new HsiSample(41.0, 0.0, 0.0, 6));

		return histo;
	}
}
