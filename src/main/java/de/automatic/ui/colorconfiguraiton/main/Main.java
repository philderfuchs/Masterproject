package de.automatic.ui.colorconfiguraiton.main;

import java.io.File;
import java.io.IOException;

import de.automatic.ui.colorconfiguraiton.clustering.AbstractKmeans;
import de.automatic.ui.colorconfiguraiton.clustering.KmeansFromGivenSeeds;
import de.automatic.ui.colorconfiguraiton.clustering.KmeansPlusPlus;
import de.automatic.ui.colorconfiguraiton.csp.ContrastUtils;
import de.automatic.ui.colorconfiguraiton.csp.Solver;
import de.automatic.ui.colorconfiguraiton.entities.SampleList;
import de.automatic.ui.colorconfiguraiton.entities.ClusterContainer;
import de.automatic.ui.colorconfiguraiton.entities.HierarchicalHsiPalette;
import de.automatic.ui.colorconfiguraiton.entities.RgbSample;
import de.automatic.ui.colorconfiguraiton.entities.Sample;
import de.automatic.ui.colorconfiguraiton.process.ImageReader;
import de.automatic.ui.colorconfiguraiton.services.ConversionService;
import de.automatic.ui.colorconfiguraiton.visualisation.HierarchicalPaletteShower;
import de.automatic.ui.colorconfiguraiton.visualisation.PaletteShower;
import de.automatic.ui.colorconfiguraiton.visualisation.ThreeDimHistogramVisualizer;
import de.automatic.ui.colorconfiguration.segmentation.Acopa;
import de.automatic.ui.colorconfiguration.segmentation.FtcSegmentation;
import de.automatic.ui.colorconfiguration.segmentation.GreyCylinderFilterer;

public class Main {

	static int k = 7;
	static int maxK = 15;
	static int attempts = 3;
	static String file = "resources/lockitup.png";

	public static double greyCylinder = 0.1;
	public static double weightFactor = 5.0;

	public static void main(String[] args) {

		SampleList hsiSamples = null;

		try {
			hsiSamples = (new ImageReader(new File(file))).getHsiHistogram(weightFactor);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		double lumSum = 0;
		for(Sample s : hsiSamples) {
			RgbSample rgb = ConversionService.toRgb(s);
			lumSum += ContrastUtils.calculateLuminance((int) rgb.getC1(), (int) rgb.getC2(),(int) rgb.getC3()) * (double) s.getCount();
		}
		double meanLuminance = lumSum / (double) hsiSamples.getCountOfPixels();
		
		hsiSamples = (new GreyCylinderFilterer()).filterGreyCylinder(hsiSamples, greyCylinder);

		System.out.println("Total Count of Pixels: " + hsiSamples.getCountOfPixels());
		
		SampleList listoForAcopa = hsiSamples;
		ClusterContainer clusters1 = null;

		System.out.println(">>>> Starting ACoPa");
		HierarchicalHsiPalette hieraPalette = new Acopa().findSeeds(listoForAcopa);
		new HierarchicalPaletteShower(hieraPalette, "Segmentation Palette", 0, 0);
		System.out.println(">>>> Finished ACoPa. Starting clustering.");
		AbstractKmeans clusterer1 = new KmeansFromGivenSeeds(hieraPalette.getSeeds());
		clusters1 = clusterer1.clusterToEnd(hsiSamples);
		System.out.println("Finished clustering with " + clusterer1.getStepCount() + " steps");
		new PaletteShower(ConversionService.toSampleList(clusters1), "K-Means after Segmentation Palette", 0, 300);	
		
		System.out.println("------------------------");
		Solver.solve(hieraPalette.getColorVars(), meanLuminance);

		// try {
		// new ThreeDimHistogramVisualizer(listoForAcopa, clusters1);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

}
