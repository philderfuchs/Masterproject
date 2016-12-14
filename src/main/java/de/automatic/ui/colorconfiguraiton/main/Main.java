package de.automatic.ui.colorconfiguraiton.main;

import java.io.File;
import java.io.IOException;

import de.automatic.ui.colorconfiguraiton.clustering.FinishingClusterer;
import de.automatic.ui.colorconfiguraiton.clustering.Kmeans;
import de.automatic.ui.colorconfiguraiton.clustering.StepByStepClusterer;
import de.automatic.ui.colorconfiguraiton.entities.Histogram;
import de.automatic.ui.colorconfiguraiton.process.ImageReader;
import de.automatic.ui.colorconfiguraiton.services.ClusterListConversionService;
import de.automatic.ui.colorconfiguraiton.vis.HistogramVisualizer;
import de.automatic.ui.colorconfiguraiton.vis.PaletteShower;

public class Main {

	static int k = 8;
	
	public static void main(String[] args) {
		Histogram histogram = null;
		try {
			histogram = (new ImageReader(new File("resources/HS.png"))).getHistogram();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FinishingClusterer c = new Kmeans(k);
		new PaletteShower(ClusterListConversionService.convertToHashSet(c.clusterToEnd(histogram)), "K-Means")
				.visualizePalette();

		new HistogramVisualizer("yo", histogram);
	}

}
