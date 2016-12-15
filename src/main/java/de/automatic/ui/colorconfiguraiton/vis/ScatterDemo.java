package de.automatic.ui.colorconfiguraiton.vis;

import java.util.ArrayList;
import java.util.Random;

import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import de.automatic.ui.colorconfiguraiton.entities.Histogram;
import de.automatic.ui.colorconfiguraiton.entities.Pixel;
import de.automatic.ui.colorconfiguraiton.entities.Channel;
import de.automatic.ui.colorconfiguraiton.entities.Cluster;

public class ScatterDemo extends AbstractAnalysis {

	private Histogram histogram;
	private ArrayList<Cluster> cluster;

	public ScatterDemo(Histogram histogram, ArrayList<Cluster> cluster) throws Exception {
		this.histogram = histogram;
		this.cluster = cluster;
		AnalysisLauncher.open(this);
	}

	public void init() {
		float x;
		float y;
		float z;
		float a;

		Coord3d[] histoPoints = new Coord3d[histogram.getLength()];
		Color[] histoColors = new Color[histogram.getLength()];

		int i = 0;
		for (Pixel p : histogram.getPixelList()) {
			x = ((float) p.get(Channel.R) / 255.0f) - 0.5f;
			y = ((float) p.get(Channel.G) / 255.0f) - 0.5f;
			z = ((float) p.get(Channel.B) / 255.0f) - 0.5f;
			histoPoints[i] = new Coord3d(x, y, z);
			histoColors[i++] = new Color(x, y, z, 0.25f);
		}

		Scatter histoScatter = new Scatter(histoPoints, histoColors);
		
		Coord3d[] clusterPoints = new Coord3d[cluster.size()];
		Color[] clusterColors = new Color[cluster.size()];
		
		i = 0;
		for (Cluster c : cluster) {
			x = ((float) c.getCenter().get(Channel.R) / 255.0f) - 0.5f;
			y = ((float) c.getCenter().get(Channel.G) / 255.0f) - 0.5f;
			z = ((float) c.getCenter().get(Channel.B) / 255.0f) - 0.5f;
			
			clusterPoints[i] = new Coord3d(x, y, z);
			clusterColors[i++] = new Color(x, y, z, 0.8f);
		}

		Scatter clusterCenterScatter = new Scatter(clusterPoints, clusterColors, 9.0f);
		
		chart = AWTChartComponentFactory.chart(Quality.Advanced, "newt");
		chart.getScene().add(histoScatter);
		chart.getScene().add(clusterCenterScatter);

	}
}