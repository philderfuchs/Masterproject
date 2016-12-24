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

			x = (float) ColorSpaceConversionService.getX(p);
			y = (float) ColorSpaceConversionService.getY(p);
			z = (float) ColorSpaceConversionService.getZ(p);
			histoPoints[i] = new Coord3d(x, y, z);
			RgbSample rgbSample = ColorSpaceConversionService.toRgb(p);
			histoColors[i] = new Color((float) rgbSample.getC1Normalized() - 0.1f,
					(float) rgbSample.getC2Normalized() - 0.1f, (float) rgbSample.getC3Normalized() - 0.1f, 0.3f);
		}

		Scatter histoScatter = new Scatter(histoPoints, histoColors);

		chart = initializeChart(Quality.Advanced);
		chart.getScene().add(histoScatter);

		if (clusters != null) {
			Coord3d[] clusterPoints = new Coord3d[clusters.size()];
			Color[] clusterColors = new Color[clusters.size()];

			int i = 0;
			for (Cluster c : clusters) {
				x = ((float) ColorSpaceConversionService.getX(c.getCenter()));
				y = ((float) ColorSpaceConversionService.getY(c.getCenter()));
				z = ((float) ColorSpaceConversionService.getZ(c.getCenter()));

				clusterPoints[i] = new Coord3d(x, y, z);
				RgbSample rgbSample = ColorSpaceConversionService.toRgb(c.getCenter());
				clusterColors[i++] =  new Color((float) rgbSample.getC1Normalized() - 0.1f,
						(float) rgbSample.getC2Normalized() - 0.1f, (float) rgbSample.getC3Normalized() - 0.1f);
			}

			Scatter clusterCenterScatter = new Scatter(clusterPoints, clusterColors, 9.0f);
			chart.getScene().add(clusterCenterScatter);
		}
	}
}