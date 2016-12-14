package de.automatic.ui.colorconfiguraiton.vis;

import java.awt.GridLayout;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

import de.automatic.ui.colorconfiguraiton.entities.Channel;
import de.automatic.ui.colorconfiguraiton.entities.Histogram;
import de.automatic.ui.colorconfiguraiton.entities.Pixel;

public class OneDimHistogramVisualizer extends JFrame {

	private static final long serialVersionUID = 1L;

	public OneDimHistogramVisualizer(String title, Histogram histogram) {
		super(title);
		this.setLayout(new GridLayout(3, 1));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.add(getChartPanel("R", histogram, Channel.R));
		this.add(getChartPanel("G", histogram, Channel.G));
		this.add(getChartPanel("B", histogram, Channel.B));

		pack();
		setVisible(true);
	}

	private ChartPanel getChartPanel(String title, Histogram histogram, Channel channel) {
		double[] values = new double[histogram.getCountOfPixels()];
		HistogramDataset dataset = new HistogramDataset();
		
		int i = 0;
		for (Pixel p : histogram.getPixelList()) {
			for(int j = 0; j < p.getCount(); j++) {
				values[i++] = (double) p.get(channel);
			}
		}
		
		dataset.addSeries("H1", values, histogram.getLength());
		JFreeChart chart = ChartFactory.createHistogram(title, "x", "y", dataset, PlotOrientation.VERTICAL, false,
				false, false);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		return chartPanel;
	}

}