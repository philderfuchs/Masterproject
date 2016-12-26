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
import de.automatic.ui.colorconfiguraiton.entities.SampleList;
import de.automatic.ui.colorconfiguraiton.entities.HsiSample;
import de.automatic.ui.colorconfiguraiton.entities.Sample;
import de.automatic.ui.colorconfiguraiton.entities.Segmentation;

public class FtcSegmentation {

	private static double threshold = 3.0;

	public void segment(SampleList histogram) {

		// Segmentation segmentation = new Segmentation(Channels.C1);
		// segmentation.add(30.0);
		// segmentation.add(60.0);
		// segmentation.add(120.0);
		// segmentation.add(240.0);
//		histogram = createSampleData();

		visualizeSegmentation(histogram, findMinima(histogram));
	}

	private Segmentation findMinima(SampleList histo) {

		Segmentation segmentation = new Segmentation(Channels.C1);

		histo.sort(Channels.C1);
		for (int i = 1; i < histo.size() - 1; i++) {
			if (histo.get(i + 1).get(Channels.C1)
					- histo.get(i).get(Channels.C1) > threshold) {
				// too far apart
				double marker = histo.get(i).get(Channels.C1)
						+ (histo.get(i +1).get(Channels.C1) - histo.get(i).get(Channels.C1))
								/ 2.0;
				segmentation.add(marker);

			} else if (histo.get(i).getCount() - histo.get(i - 1).getCount() < 0.0) {
				if (histo.get(i + 1).getCount() - histo.get(i).getCount() > 0.0) {
					segmentation.add(histo.get(i).get(Channels.C1));
				} else if (histo.get(i + 1).getCount() - histo.get(i).getCount() == 0.0) {
					// maybe plateu
					int j = i + 1;
					while (j < histo.size() - 1
							&& histo.get(j + 1).getCount() - histo.get(j).getCount() == 0.0) {
						j++;
					}
					if (j < histo.size() - 1
							&& histo.get(j + 1).getCount() - histo.get(j).getCount() > 0.0) {
						double marker = histo.get(i).get(Channels.C1)
								+ (histo.get(j).get(Channels.C1)
										- histo.get(i).get(Channels.C1)) / 2.0;
						segmentation.add(marker);
						i = j - 1;
					}
				}
			}
		}
		System.out.println(segmentation.size());
		return segmentation;

	}

	public void visualizeSegmentation(SampleList histogram, Segmentation segmentation) {
		JFrame frame = new JFrame("Segmentation Visualization");
		frame.setLayout(new GridLayout(2, 1));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JFreeChart chart = getChart(histogram);

		XYPlot plot = (XYPlot) chart.getPlot();

		for (Double d : segmentation) {
			ValueMarker marker = new ValueMarker(d);
			marker.setPaint(Color.BLACK);
			marker.setLabel("Marker");
			plot.addDomainMarker(marker);
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

	private JFreeChart getChart(SampleList histogram) {
		double[] values = new double[histogram.getCountOfPixels()];
		HistogramDataset dataset = new HistogramDataset();

		int i = 0;
		for (Sample p : histogram) {
			for (int j = 0; j < p.getCount(); j++) {
				values[i++] = (double) p.get(Channels.C1);
			}
		}

		dataset.addSeries("H1", values, (int) histogram.get(histogram.size() - 1).get(Channels.C1));
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
