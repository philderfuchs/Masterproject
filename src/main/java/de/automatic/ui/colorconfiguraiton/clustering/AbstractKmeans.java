package de.automatic.ui.colorconfiguraiton.clustering;

import java.util.ArrayList;

import de.automatic.ui.colorconfiguraiton.entities.*;
import de.automatic.ui.colorconfiguraiton.services.CalculationService;
import de.automatic.ui.colorconfiguraiton.services.ConversionService;
import de.automatic.ui.colorconfiguraiton.services.ErrorCalculationService;

public abstract class AbstractKmeans implements StepByStepClusterer, FinishingClusterer {

	protected static int maxStepCount = 40;

	protected int k;
	protected ClusterContainer clusters;
	protected boolean finished;

	protected int stepCount = 0;

	@Override
	public ClusterContainer clusterToEnd(SampleList histogram) {
		this.init(histogram);

		while (!finished) {
			this.step(histogram);
		}
		return clusters;
	}

	public abstract ClusterContainer init(SampleList histogram);

	public ClusterContainer step(SampleList histogram) {
		System.out.println("step");
		stepCount++;
		this.finished = true;

		if (this.clusters == null) {
			System.out.println("init from step");
			this.init(histogram);
		}

		for (Cluster c : clusters) {
			if (c.getSampleList().size() == 0) {
				continue;
			}

			Sample newMean = CalculationService.calculateMean(c.getSampleList());
			if (!newMean.equals(c.getCenter())) {
				c.setCenter(newMean);
				this.finished = false;
			}
			if (stepCount == maxStepCount) {
				this.finished = true;
			}
		}

		reassignPixelsToCluster(histogram, clusters);

		return clusters;
	}

	protected void reassignPixelsToCluster(SampleList histogram, ClusterContainer clusters) {
		for (Cluster c : clusters) {
			c.getSampleList().clear();
		}
		// put each pixel in the histogram in the closest cluster
		for (Sample p : histogram) {
			double minDistance = Double.MAX_VALUE;
			Cluster closestCluster = null;
			for (Cluster c : clusters) {
				double currentDistance = ErrorCalculationService.getEucledianDistance(p, c.getCenter());
				if (currentDistance < minDistance) {
					minDistance = currentDistance;
					closestCluster = c;
				}
			}
			closestCluster.getSampleList().add(p);
		}
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

	public ClusterContainer getClusters() {
		return clusters;
	}

	public int getStepCount() {
		return stepCount;
	}

}
