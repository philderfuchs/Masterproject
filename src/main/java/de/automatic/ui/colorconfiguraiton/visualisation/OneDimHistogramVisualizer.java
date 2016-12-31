package de.automatic.ui.colorconfiguraiton.visualisation;

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
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import de.automatic.ui.colorconfiguraiton.entities.Channels;
import de.automatic.ui.colorconfiguraiton.entities.SampleList;
import de.automatic.ui.colorconfiguraiton.entities.Segmentation;
import de.automatic.ui.colorconfiguraiton.entities.RgbSample;
import de.automatic.ui.colorconfiguraiton.entities.Sample;
import de.automatic.ui.colorconfiguraiton.entities.Cluster;
import de.automatic.ui.colorconfiguraiton.entities.Histogram;

public class OneDimHistogramVisualizer extends JFrame {
	
	public OneDimHistogramVisualizer(String title, Histogram histogram, Segmentation segmentation, int x, int y, int width, int height){
		super(title);
		this.setLayout(new GridLayout(1, 1));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		visualizeSegmentation(histogram, segmentation);
		
		this.add(visualizeSegmentation(histogram, segmentation));
		this.pack();
		this.setLocation(x, y);
		this.setSize(width, height);
		this.setVisible(true);
		
	}

	public ChartPanel visualizeSegmentation(Histogram histogram, Segmentation segmentation) {


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
		
		return chartPanel;
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

}