package de.automatic.ui.colorconfiguraiton.main;

import java.io.File;
import java.io.IOException;

import de.automatic.ui.colorconfiguraiton.clustering.FinishingClusterer;
import de.automatic.ui.colorconfiguraiton.clustering.Kmeans;
import de.automatic.ui.colorconfiguraiton.clustering.StepByStepClusterer;
import de.automatic.ui.colorconfiguraiton.entities.Channel;
import de.automatic.ui.colorconfiguraiton.entities.Histogram;
import de.automatic.ui.colorconfiguraiton.process.ImageReader;
import de.automatic.ui.colorconfiguraiton.services.ClusterListConversionService;
import de.automatic.ui.colorconfiguraiton.vis.OneDimHistogramVisualizer;
import de.automatic.ui.colorconfiguraiton.vis.PaletteShower;

public class Main {

	static int k = 8;
	
	public static void main(String[] args) {
		Histogram histogram = null;
		try {
			histogram = (new ImageReader(new File("resources/kanye_small.jpg"))).getHistogram();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FinishingClusterer c = new Kmeans(k);
		new PaletteShower(ClusterListConversionService.convertToHashSet(c.clusterToEnd(histogram)), "K-Means")
				.visualizePalette();

		new OneDimHistogramVisualizer("yo", histogram, Channel.R);
		new OneDimHistogramVisualizer("yo", histogram, Channel.G);
		new OneDimHistogramVisualizer("yo", histogram, Channel.B);
		
	}

}
