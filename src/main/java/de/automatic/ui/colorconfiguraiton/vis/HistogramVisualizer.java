package de.automatic.ui.colorconfiguraiton.vis;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

import de.automatic.ui.colorconfiguraiton.entities.Histogram;

public class HistogramVisualizer extends JFrame {

	private static final long serialVersionUID = 1L;

	public HistogramVisualizer(String title, Histogram histogram) {
		super(title);

		HistogramDataset dataset = new HistogramDataset();
		double[] values = { 1.0, 2.0, 2.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0};
		dataset.addSeries("H1", values, 10);
		JFreeChart chart = ChartFactory.createHistogram(title, "x", "y", dataset, PlotOrientation.VERTICAL, false,
				false, false);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);

		pack();
		setVisible(true);
	}

}