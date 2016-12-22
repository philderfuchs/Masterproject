package de.automatic.ui.colorconfiguraiton.clustering;

import java.util.ArrayList;

import de.automatic.ui.colorconfiguraiton.entities.*;
import de.automatic.ui.colorconfiguraiton.services.ErrorCalculationService;

public abstract class AbstractKmeans implements StepByStepClusterer, FinishingClusterer {

	protected int k;
	protected ClusterContainer clusters;
	protected boolean finished;

	protected int stepCount = 0;

	@Override
	public ClusterContainer clusterToEnd(Histogram histogram) {
		this.init(histogram);

		while (!finished) {
			this.step(histogram);
		}
		return clusters;
	}

	public abstract ClusterContainer init(Histogram histogram);

	public ClusterContainer step(Histogram histogram) {
		stepCount++;
		this.finished = true;

		if (this.clusters == null) {
			this.init(histogram);
		}

		for (Cluster c : clusters) {
			if (c.getHistogram().getLength() == 0) {
				continue;
			}

			long meanR = 0;
			long meanG = 0;
			long meanB = 0;
			for (Sample p : c.getHistogram().getPixelList()) {
				meanR += p.getC1() * p.getCount();
				meanG += p.getC2() * p.getCount();
				meanB += p.getC3() * p.getCount();
			}
			meanR = meanR / c.getHistogram().getCountOfPixels();
			meanG = meanG / c.getHistogram().getCountOfPixels();
			meanB = meanB / c.getHistogram().getCountOfPixels();
			RgbSample newMean = new RgbSample((int) meanR, (int) meanG, (int) meanB, 1);
			if (!newMean.sameAs(c.getCenter())) {
				c.setCenter(newMean);
				this.finished = false;
			}
		}

		reassignPixelsToCluster(histogram, clusters);

		return clusters;
	}

	protected void reassignPixelsToCluster(Histogram histogram, ClusterContainer clusters) {
		for (Cluster c : clusters) {
			c.getHistogram().clear();
		}
		// put each pixel in the histogram in the closest cluster
		for (Sample p : histogram.getPixelList()) {
			double minDistance = Double.MAX_VALUE;
			Cluster closestCluster = null;
			for (Cluster c : clusters) {
				double currentDistance = ErrorCalculationService.getEucledianDistance(p, c.getCenter());
				if (currentDistance < minDistance) {
					minDistance = currentDistance;
					closestCluster = c;
				}
			}
			closestCluster.getHistogram().add(p);
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
