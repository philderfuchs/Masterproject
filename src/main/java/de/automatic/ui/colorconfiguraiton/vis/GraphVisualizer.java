package de.automatic.ui.colorconfiguraiton.vis;

import org.jfree.chart.ChartPanel;

import java.util.ArrayList;

import org.apache.commons.math3.util.Pair;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import de.automatic.ui.colorconfiguraiton.entities.RgbPixel;

import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultKeyedValues2DDataset;
import org.jfree.data.xy.DefaultTableXYDataset;
import org.jfree.data.xy.XYSeries;

public class GraphVisualizer extends ApplicationFrame {

	public GraphVisualizer(ArrayList<Pair<Double, Double>> list) {
		super("");
		JFreeChart lineChart = ChartFactory.createLineChart("k to Error", "k", "Error", createDataset(list),
				PlotOrientation.VERTICAL, true, true, false);

		ChartPanel chartPanel = new ChartPanel(lineChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
		setContentPane(chartPanel);

		pack();
		RefineryUtilities.centerFrameOnScreen(this);
		setVisible(true);
	}

	private CategoryDataset createDataset(ArrayList<Pair<Double, Double>> list) {
		DefaultKeyedValues2DDataset dataset = new DefaultKeyedValues2DDataset();
		for(Pair<Double, Double> pair : list) {
			dataset.addValue(pair.getValue(), "k", pair.getKey());
		}
		
		return dataset;
	}

}
