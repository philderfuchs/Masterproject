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
import de.automatic.ui.colorconfiguraiton.entities.HsiSample;
import de.automatic.ui.colorconfiguraiton.entities.RgbSample;
import de.automatic.ui.colorconfiguraiton.entities.Sample;
import de.automatic.ui.colorconfiguraiton.services.ColorSpaceConversionService;
import de.automatic.ui.colorconfiguraiton.entities.Channels;
import de.automatic.ui.colorconfiguraiton.entities.Cluster;
import de.automatic.ui.colorconfiguraiton.entities.ClusterContainer;

public class ThreeDimHistogramVisualizer extends AbstractAnalysis {

	private Histogram histogram;
	private ArrayList<Cluster> clusters;

	public ThreeDimHistogramVisualizer(Histogram histogram, ClusterContainer clusters) throws Exception {
		this.histogram = histogram;
		this.clusters = clusters;
		AnalysisLauncher.open(this);
	}

	public void init() {
		float x;
		float y;
		float z;
		float a;

		int size = histogram.getLength();

		Coord3d[] histoPoints = new Coord3d[size];
		Color[] histoColors = new Color[size];

		// int i = 0;
		// for (Pixel p : histogram.getPixelList()) {
		for (int i = 0; i < size; i++) {
			Sample p = histogram.getPixelList().get(i);
			if (p instanceof HsiSample) {

				x = (float) ColorSpaceConversionService.getX((HsiSample) p);
				y = (float) ColorSpaceConversionService.getY((HsiSample) p);
				z = (float) (p.getC3()) - 0.5f;
				histoPoints[i] = new Coord3d(x, y, z);
				histoColors[i] = new Color(0, 0, 0, 0.3f);

			} else if (p instanceof RgbSample) {
				x = ((float) p.get(Channels.C1) / 255.0f);
				y = ((float) p.get(Channels.C2) / 255.0f);
				z = ((float) p.get(Channels.C3) / 255.0f);
				histoPoints[i] = new Coord3d(x - 0.5f, y - 0.5f, z - 0.5f);
				histoColors[i] = new Color(x - 0.1f, y - 0.1f, z - 0.1f, 0.3f);
			}
			// a = ((float) p.getCount()) / ((float)
			// histogram.getCountOfPixels());

		}

		Scatter histoScatter = new Scatter(histoPoints, histoColors);

		chart = initializeChart(Quality.Advanced);
		chart.getScene().add(histoScatter);

		if (clusters != null) {
			Coord3d[] clusterPoints = new Coord3d[clusters.size()];
			Color[] clusterColors = new Color[clusters.size()];

			int i = 0;
			for (Cluster c : clusters) {
				x = ((float) c.getCenter().get(Channels.C1) / 255.0f);
				y = ((float) c.getCenter().get(Channels.C2) / 255.0f);
				z = ((float) c.getCenter().get(Channels.C3) / 255.0f);
				a = 0.8f;

				clusterPoints[i] = new Coord3d(x - 0.5f, y - 0.5f, z - 0.5f);
				clusterColors[i++] = new Color(x - 0.1f, y - 0.1f, z - 0.1f);
			}

			Scatter clusterCenterScatter = new Scatter(clusterPoints, clusterColors, 9.0f);
			chart.getScene().add(clusterCenterScatter);
		}
	}
}