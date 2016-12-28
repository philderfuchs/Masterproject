package de.automatic.ui.colorconfiguraiton.main;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.Pair;

import de.automatic.ui.colorconfiguraiton.clustering.AbstractKmeans;
import de.automatic.ui.colorconfiguraiton.clustering.FinishingClusterer;
import de.automatic.ui.colorconfiguraiton.clustering.FtcSegmentation;
import de.automatic.ui.colorconfiguraiton.clustering.KmeansFromGivenSeeds;
import de.automatic.ui.colorconfiguraiton.clustering.KmeansPlusPlus;
import de.automatic.ui.colorconfiguraiton.clustering.RandomSeedKmeans;
import de.automatic.ui.colorconfiguraiton.clustering.StepByStepClusterer;
import de.automatic.ui.colorconfiguraiton.entities.SampleList;
import de.automatic.ui.colorconfiguraiton.entities.HsiSample;
import de.automatic.ui.colorconfiguraiton.entities.RgbSample;
import de.automatic.ui.colorconfiguraiton.entities.Cluster;
import de.automatic.ui.colorconfiguraiton.entities.ClusterContainer;
import de.automatic.ui.colorconfiguraiton.process.ImageReader;
import de.automatic.ui.colorconfiguraiton.services.ConversionService;
import de.automatic.ui.colorconfiguraiton.vis.GraphVisualizer;
import de.automatic.ui.colorconfiguraiton.vis.OneDimHistogramVisualizer;
import de.automatic.ui.colorconfiguraiton.vis.PaletteShower;
import de.automatic.ui.colorconfiguraiton.vis.ThreeDimHistogramVisualizer;

public class Main {

	static int i = 1;
	static int k = 5;
	static int maxK = 15;
	static int attempts = 3;
	static String file = "resources/lockitup.png";

	public static void main(String[] args) {

		// for (int i = 0; i < 2; i++) {
		SampleList hsiSamples = null;
		SampleList rgbSamples = null;

		try {
			hsiSamples = (new ImageReader(new File(file))).getHsiHistogram();
			rgbSamples = (new ImageReader(new File(file))).getRgbHistogram();
		} catch (IOException e) {
			e.printStackTrace();
		}
		SampleList seeds = new FtcSegmentation().segment(hsiSamples);
		new PaletteShower(ConversionService.toHashSet(seeds), "Segmentation Palette").visualizePalette();

		AbstractKmeans clusterer1 = new KmeansFromGivenSeeds(ConversionService.toRgbSampleList(seeds));
		System.out.println("start clustering");
		ClusterContainer clusters1 = clusterer1.clusterToEnd(rgbSamples);
		System.out.println("finished clustering with " + clusterer1.getStepCount() + " steps");
		new PaletteShower(ConversionService.toHashSet(clusters1), "K-Means after Segmentation Palette").visualizePalette();

		AbstractKmeans clusterer2 = new KmeansPlusPlus(k);
		System.out.println("start clustering");
		ClusterContainer clusters2 = clusterer2.clusterToEnd(rgbSamples);
		System.out.println("finished clustering with " + clusterer2.getStepCount() + " steps");
		new PaletteShower(ConversionService.toHashSet(clusters2), "K-Means Palette").visualizePalette();

		try {
			new ThreeDimHistogramVisualizer(hsiSamples, clusters1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// new OneDimHistogramVisualizer("Channel Histograms", histogram,
		// clusters);
	}

	// public static void main(String[] args) {
	//
	// ArrayList<Pair<Double, Double>> list = new ArrayList<Pair<Double,
	// Double>>();
	// Histogram histogram = null;
	// try {
	// histogram = (new ImageReader(new File(file))).getHistogram();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// for (int k = 1; k <= maxK; k++) {
	// double accError = 0;
	// for (int a = 0; a < attempts; a++) {
	//
	// AbstractKmeans clusterer = new KmeansPlusPlus(k);
	// ClusterContainer clusters = clusterer.clusterToEnd(histogram);
	// accError += clusters.getError();
	// }
	// double meanError = accError / (double) attempts;
	// list.add(new Pair<Double, Double>((double) k, meanError));
	//
	// }
	//
	// new GraphVisualizer(list);
	// }

}
