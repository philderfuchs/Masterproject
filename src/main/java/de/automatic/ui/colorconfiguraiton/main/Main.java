package de.automatic.ui.colorconfiguraiton.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.automatic.ui.colorconfiguraiton.clustering.Kmeans;
import de.automatic.ui.colorconfiguraiton.entities.Histogram;
import de.automatic.ui.colorconfiguraiton.entities.Cluster;
import de.automatic.ui.colorconfiguraiton.process.ImageReader;
import de.automatic.ui.colorconfiguraiton.services.ClusterListConversionService;
import de.automatic.ui.colorconfiguraiton.vis.OneDimHistogramVisualizer;
import de.automatic.ui.colorconfiguraiton.vis.PaletteShower;

public class Main {

	static int k = 2;
	
	public static void main(String[] args) {
		Histogram histogram = null;
		try {
			histogram = (new ImageReader(new File("resources/HS.png"))).getHistogram();
		} catch (IOException e) {
			e.printStackTrace();
		}
		ArrayList<Cluster> clusters = new Kmeans(k).clusterToEnd(histogram);
		new PaletteShower(ClusterListConversionService.convertToHashSet(clusters), "K-Means")
				.visualizePalette();

		new OneDimHistogramVisualizer("Channel Histograms", histogram, clusters);	
	}

}
