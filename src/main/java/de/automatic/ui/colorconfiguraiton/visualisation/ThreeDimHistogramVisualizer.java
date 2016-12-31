package de.automatic.ui.colorconfiguraiton.visualisation;

import java.util.ArrayList;
import java.util.Random;

import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import de.automatic.ui.colorconfiguraiton.entities.SampleList;
import de.automatic.ui.colorconfiguraiton.entities.HsiSample;
import de.automatic.ui.colorconfiguraiton.entities.RgbSample;
import de.automatic.ui.colorconfiguraiton.entities.Sample;
import de.automatic.ui.colorconfiguraiton.services.ConversionService;
import de.automatic.ui.colorconfiguraiton.entities.CartesianCoordinates;
import de.automatic.ui.colorconfiguraiton.entities.Channels;
import de.automatic.ui.colorconfiguraiton.entities.Cluster;
import de.automatic.ui.colorconfiguraiton.entities.ClusterContainer;

public class ThreeDimHistogramVisualizer extends AbstractAnalysis {

	private SampleList samples;
	private ArrayList<Cluster> clusters;

	public ThreeDimHistogramVisualizer(SampleList histogram, ClusterContainer clusters) throws Exception {
		this.samples = histogram;
		this.clusters = clusters;
		AnalysisLauncher.open(this);
	}

	public void init() {

		int size = samples.size();

		Coord3d[] histoPoints = new Coord3d[size];
		Color[] histoColors = new Color[size];

		// int i = 0;
		// for (Pixel p : histogram.getPixelList()) {
		for (int i = 0; i < size; i++) {
			Sample s = samples.get(i);
			CartesianCoordinates coord = ConversionService.toCoordinates(s);
			histoPoints[i] = new Coord3d(coord.getX(), coord.getY(), coord.getZ());
			RgbSample rgbSample = ConversionService.toRgb(s);
			histoColors[i] = new Color((float) rgbSample.getC1Normalized() - 0.1f,
					(float) rgbSample.getC2Normalized() - 0.1f, (float) rgbSample.getC3Normalized() - 0.1f, 0.3f);
		}

		Scatter histoScatter = new Scatter(histoPoints, histoColors);

		chart = initializeChart(Quality.Advanced);
		chart.getScene().add(histoScatter);

		if (clusters != null) {
			Coord3d[] clusterPoints = new Coord3d[clusters.size()];
			Color[] clusterColors = new Color[clusters.size()];

			SampleList centers = new SampleList();
			for (Cluster c : clusters) {
				if (samples.get(0) instanceof RgbSample) {
					centers.add(ConversionService.toRgb(c.getCenter()));
				} else if (samples.get(0) instanceof HsiSample) {
					centers.add(ConversionService.toHsi(c.getCenter()));
				}
			}

			int i = 0;
			for (Sample s : centers) {
				CartesianCoordinates coord = ConversionService.toCoordinates(s);
				clusterPoints[i] = new Coord3d(coord.getX(), coord.getY(), coord.getZ());
				RgbSample rgbSample = ConversionService.toRgb(s);
				clusterColors[i++] = new Color((float) rgbSample.getC1Normalized() - 0.1f,
						(float) rgbSample.getC2Normalized() - 0.1f, (float) rgbSample.getC3Normalized() - 0.1f);
			}

			Scatter clusterCenterScatter = new Scatter(clusterPoints, clusterColors, 9.0f);
			chart.getScene().add(clusterCenterScatter);
		}
	}
}