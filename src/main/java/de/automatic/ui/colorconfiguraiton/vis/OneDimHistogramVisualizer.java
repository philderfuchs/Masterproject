package de.automatic.ui.colorconfiguraiton.vis;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;

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
import de.automatic.ui.colorconfiguraiton.entities.RgbSample;
import de.automatic.ui.colorconfiguraiton.entities.Sample;
import de.automatic.ui.colorconfiguraiton.entities.Cluster;

public class OneDimHistogramVisualizer extends JFrame {

	private static final long serialVersionUID = 1L;

	public OneDimHistogramVisualizer(String title, Histogram histogram, ArrayList<Cluster> clusters) {
		super(title);
		this.setLayout(new GridLayout(3, 1));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.add(getChartPanel("C1", histogram, Channels.C1, clusters));
		this.add(getChartPanel("C2", histogram, Channels.C2, clusters));
		this.add(getChartPanel("C3", histogram, Channels.C3, clusters));

		pack();
		setVisible(true);
	}

	private ChartPanel getChartPanel(String title, Histogram histogram, Channels channel, ArrayList<Cluster> clusters) {
		double[] values = new double[histogram.getCountOfPixels()];
		HistogramDataset dataset = new HistogramDataset();

		int i = 0;
		for (Sample p : histogram.getSamples()) {
			for (int j = 0; j < p.getCount(); j++) {
				values[i++] = (double) p.get(channel);
			}
		}

		dataset.addSeries("H1", values, histogram.getLength());
		JFreeChart chart = ChartFactory.createHistogram(title, "C", "Count", dataset, PlotOrientation.VERTICAL, false,
				false, false);

		XYPlot plot = (XYPlot) chart.getPlot();

//		for (Cluster c : clusters) {
//			ValueMarker marker = new ValueMarker(c.getCenter().get(channel));
//			marker.setPaint(new Color(c.getCenter().get(Channel.R), c.getCenter().get(Channel.G),
//					c.getCenter().get(Channel.B)));
//			marker.setLabel("yo");
//			plot.addDomainMarker(marker);
//		}

		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(1000, 270));
		return chartPanel;
	}

}