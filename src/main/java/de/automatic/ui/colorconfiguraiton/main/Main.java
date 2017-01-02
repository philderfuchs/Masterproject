package de.automatic.ui.colorconfiguraiton.main;

import java.io.File;
import java.io.IOException;

import de.automatic.ui.colorconfiguraiton.clustering.AbstractKmeans;
import de.automatic.ui.colorconfiguraiton.clustering.KmeansFromGivenSeeds;
import de.automatic.ui.colorconfiguraiton.clustering.KmeansPlusPlus;
import de.automatic.ui.colorconfiguraiton.entities.SampleList;
import de.automatic.ui.colorconfiguraiton.entities.ClusterContainer;
import de.automatic.ui.colorconfiguraiton.entities.HierarchicalHsiPalette;
import de.automatic.ui.colorconfiguraiton.process.ImageReader;
import de.automatic.ui.colorconfiguraiton.services.ConversionService;
import de.automatic.ui.colorconfiguraiton.visualisation.HierarchicalPaletteShower;
import de.automatic.ui.colorconfiguraiton.visualisation.PaletteShower;
import de.automatic.ui.colorconfiguraiton.visualisation.ThreeDimHistogramVisualizer;
import de.automatic.ui.colorconfiguration.segmentation.Acopa;
import de.automatic.ui.colorconfiguration.segmentation.FtcSegmentation;

public class Main {

	static int i = 1;
	static int k = 5;
	static int maxK = 15;
	static int attempts = 3;
	static String file = "resources/kanye_small.jpg";

	public static void main(String[] args) {

		SampleList hsiSamples = null;
		SampleList rgbSamples = null;

		try {
			hsiSamples = (new ImageReader(new File(file))).getHsiHistogram();
			rgbSamples = (new ImageReader(new File(file))).getRgbHistogram();
		} catch (IOException e) {
			e.printStackTrace();
		}

		SampleList listoForAcopa = hsiSamples;

		HierarchicalHsiPalette hieraPalette = new Acopa().findSeeds(listoForAcopa);
		new HierarchicalPaletteShower(hieraPalette, "Segmentation Palette", 0, 0);

		AbstractKmeans clusterer1 = new KmeansFromGivenSeeds(ConversionService.toRgbSampleList(hieraPalette.getSeeds()));
		System.out.println("start clustering");
		ClusterContainer clusters1 = clusterer1.clusterToEnd(rgbSamples);
		System.out.println("finished clustering with " + clusterer1.getStepCount() + " steps");
		new PaletteShower(ConversionService.toHashSet(clusters1), "K-Means after Segmentation Palette", 1000, 300);

		AbstractKmeans clusterer2 = new KmeansPlusPlus(k);
		System.out.println("start clustering");
		ClusterContainer clusters2 = clusterer2.clusterToEnd(rgbSamples);
		System.out.println("finished clustering with " + clusterer2.getStepCount() + " steps");
		new PaletteShower(ConversionService.toHashSet(clusters2), "K-Means Palette", 1000, 600);

		try {
			new ThreeDimHistogramVisualizer(listoForAcopa, clusters1);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
