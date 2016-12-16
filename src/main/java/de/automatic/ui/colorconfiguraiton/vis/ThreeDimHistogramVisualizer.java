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

public class ThreeDimHistogramVisualizer extends AbstractAnalysis {

	private Histogram histogram;
	private ArrayList<Cluster> clusters;

	public ThreeDimHistogramVisualizer(Histogram histogram, ArrayList<Cluster> clusters) throws Exception {
		this.histogram = histogram;
		this.clusters = clusters;
		AnalysisLauncher.open(this);
	}

	public void init() {
		float x;
		float y;
		float z;
		float a;

//		int size = Math.min(histogram.getLength(), 100000);
		int size = histogram.getLength();


		Coord3d[] histoPoints = new Coord3d[size];
		Color[] histoColors = new Color[size];

		// int i = 0;
		// for (Pixel p : histogram.getPixelList()) {
		for (int i = 0; i < size; i++) {
			Pixel p = histogram.getPixelList().get(i);
			x = ((float) p.get(Channel.R) / 255.0f);
			y = ((float) p.get(Channel.G) / 255.0f);
			z = ((float) p.get(Channel.B) / 255.0f);
			// a = ((float) p.getCount()) / ((float)
			// histogram.getCountOfPixels());

			histoPoints[i] = new Coord3d(x - 0.5f, y - 0.5f, z - 0.5f);
			histoColors[i] = new Color(x - 0.1f, y - 0.1f, z - 0.1f, 0.3f);
		}

		Scatter histoScatter = new Scatter(histoPoints, histoColors);

		Coord3d[] clusterPoints = new Coord3d[clusters.size()];
		Color[] clusterColors = new Color[clusters.size()];

		int i = 0;
		for (Cluster c : clusters) {
			x = ((float) c.getCenter().get(Channel.R) / 255.0f);
			y = ((float) c.getCenter().get(Channel.G) / 255.0f);
			z = ((float) c.getCenter().get(Channel.B) / 255.0f);
			a = 0.8f;

			clusterPoints[i] = new Coord3d(x - 0.5f, y - 0.5f, z - 0.5f);
			clusterColors[i++] = new Color(x - 0.1f, y - 0.1f, z - 0.1f);
		}

		Scatter clusterCenterScatter = new Scatter(clusterPoints, clusterColors, 9.0f);

		chart = AWTChartComponentFactory.chart(Quality.Advanced, "newt");
		chart.getScene().add(histoScatter);
		chart.getScene().add(clusterCenterScatter);

	}
}