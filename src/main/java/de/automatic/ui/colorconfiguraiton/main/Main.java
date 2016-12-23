package de.automatic.ui.colorconfiguraiton.main;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.util.Pair;

import de.automatic.ui.colorconfiguraiton.clustering.AbstractKmeans;
import de.automatic.ui.colorconfiguraiton.clustering.FinishingClusterer;
import de.automatic.ui.colorconfiguraiton.clustering.KmeansPlusPlus;
import de.automatic.ui.colorconfiguraiton.clustering.RandomSeedKmeans;
import de.automatic.ui.colorconfiguraiton.clustering.StepByStepClusterer;
import de.automatic.ui.colorconfiguraiton.entities.Histogram;
import de.automatic.ui.colorconfiguraiton.entities.HsiSample;
import de.automatic.ui.colorconfiguraiton.entities.RgbSample;
import de.automatic.ui.colorconfiguraiton.entities.Cluster;
import de.automatic.ui.colorconfiguraiton.entities.ClusterContainer;
import de.automatic.ui.colorconfiguraiton.process.ImageReader;
import de.automatic.ui.colorconfiguraiton.services.ClusterListConversionService;
import de.automatic.ui.colorconfiguraiton.services.ColorSpaceConversionService;
import de.automatic.ui.colorconfiguraiton.vis.GraphVisualizer;
import de.automatic.ui.colorconfiguraiton.vis.OneDimHistogramVisualizer;
import de.automatic.ui.colorconfiguraiton.vis.PaletteShower;
import de.automatic.ui.colorconfiguraiton.vis.ThreeDimHistogramVisualizer;

public class Main {

	static int k = 5;
	static int maxK = 15;
	static int attempts = 3;
	static String file = "resources/HS.png";

	public static void main(String[] args) {

		Histogram histogram = null;
		try {
			histogram = (new ImageReader(new File(file))).getHsiHistogram();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ClusterContainer clusters = null;

//		AbstractKmeans clusterer = new KmeansPlusPlus(k);
//		clusters = clusterer.clusterToEnd(histogram);
//		new PaletteShower(ClusterListConversionService.convertToHashSet(clusters), "K-Means").visualizePalette();

		// new OneDimHistogramVisualizer("Channel Histograms", histogram,
		// clusters);

		try {
			new ThreeDimHistogramVisualizer(histogram, clusters);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
//	public static void main(String[] args) {
//
//		ArrayList<Pair<Double, Double>> list = new ArrayList<Pair<Double, Double>>();
//		Histogram histogram = null;
//		try {
//			histogram = (new ImageReader(new File(file))).getHistogram();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//
//		for (int k = 1; k <= maxK; k++) {
//			double accError = 0;
//			for (int a = 0; a < attempts; a++) {
//
//				AbstractKmeans clusterer = new KmeansPlusPlus(k);
//				ClusterContainer clusters = clusterer.clusterToEnd(histogram);
//				accError += clusters.getError();
//			}
//			double meanError = accError / (double) attempts;
//			list.add(new Pair<Double, Double>((double) k, meanError));
//
//		}
//
//		new GraphVisualizer(list);
//	}

}
