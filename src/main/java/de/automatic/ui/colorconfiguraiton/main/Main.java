package de.automatic.ui.colorconfiguraiton.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.automatic.ui.colorconfiguraiton.clustering.KmeansPlusPlus;
import de.automatic.ui.colorconfiguraiton.clustering.RandomSeedKmeans;
import de.automatic.ui.colorconfiguraiton.entities.Histogram;
import de.automatic.ui.colorconfiguraiton.entities.Cluster;
import de.automatic.ui.colorconfiguraiton.process.ImageReader;
import de.automatic.ui.colorconfiguraiton.services.ClusterListConversionService;
import de.automatic.ui.colorconfiguraiton.vis.OneDimHistogramVisualizer;
import de.automatic.ui.colorconfiguraiton.vis.PaletteShower;
import de.automatic.ui.colorconfiguraiton.vis.ThreeDimHistogramVisualizer;

public class Main {

	static int k = 5;
	static String file = "resources/macmiller.png";

	public static void main(String[] args) {
		Histogram histogram = null;
		try {
			histogram = (new ImageReader(new File(file))).getHistogram();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<Cluster> clusters = new KmeansPlusPlus(k).clusterToEnd(histogram);
		new PaletteShower(ClusterListConversionService.convertToHashSet(clusters), "K-Means").visualizePalette();

		// new OneDimHistogramVisualizer("Channel Histograms", histogram,
		// clusters);

		try {
			new ThreeDimHistogramVisualizer(histogram, clusters);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
