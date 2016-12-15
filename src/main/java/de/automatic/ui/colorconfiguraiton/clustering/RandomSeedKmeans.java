package de.automatic.ui.colorconfiguraiton.clustering;

import java.util.ArrayList;

import de.automatic.ui.colorconfiguraiton.entities.*;

public class RandomSeedKmeans extends AbstractKmeans implements StepByStepClusterer, FinishingClusterer {

	public RandomSeedKmeans(int k) {
		this.k = k;
		this.finished = false;
	}

	public ArrayList<Cluster> init(Histogram histogram) {
		this.finished = false;
		// clusters not yet initialized
		clusters = new ArrayList<Cluster>();
		// initialize random clusters
		for (int i = 0; i < k; i++) {
			clusters.add(new Cluster(new Histogram(), new Pixel((int) (Math.random() * 255),
					(int) (Math.random() * 255), (int) (Math.random() * 255), 1)));
		}

		reassignPixelsToCluster(histogram, clusters);

		return clusters;
	}

}
