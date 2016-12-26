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
import de.automatic.ui.colorconfiguraiton.entities.Sample;
import de.automatic.ui.colorconfiguraiton.entities.Segmentation;

public class FtcSegmentation {

	public void segment(Histogram histogram) {
		
		Segmentation segmentation = new Segmentation(Channels.C1);
		segmentation.add(30.0);
		segmentation.add(60.0);
		segmentation.add(120.0);
		segmentation.add(240.0);
		visualizeSegmentation(histogram, segmentation);
	}

	public void visualizeSegmentation(Histogram histogram, Segmentation segmentation) {
		JFrame frame = new JFrame ("Segmentation Visualization");
		frame.setLayout(new GridLayout(1, 1));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		double[] values = new double[histogram.getCountOfPixels()];
		HistogramDataset dataset = new HistogramDataset();

		int i = 0;
		for (Sample p : histogram.getPixelList()) {
			for (int j = 0; j < p.getCount(); j++) {
				values[i++] = (double) p.get(Channels.C1);
			}
		}

		dataset.addSeries("H1", values, histogram.getLength());
		JFreeChart chart = ChartFactory.createHistogram("Segmentation", "C1", "Count", dataset, PlotOrientation.VERTICAL, false,
				false, false);

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
		frame.pack();
		frame.setVisible(true);
	}
	
}
