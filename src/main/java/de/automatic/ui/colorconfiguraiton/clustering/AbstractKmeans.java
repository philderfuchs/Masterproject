package de.automatic.ui.colorconfiguraiton.clustering;

import java.util.ArrayList;

import de.automatic.ui.colorconfiguraiton.entities.*;
import de.automatic.ui.colorconfiguraiton.services.ErrorCalculationService;

public abstract class AbstractKmeans implements StepByStepClusterer, FinishingClusterer {

	protected int maxStepCount = 30;
	
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
		System.out.println("step");
		stepCount++;
		this.finished = true;

		if (this.clusters == null) {
			this.init(histogram);
		}

		for (Cluster c : clusters) {
			if (c.getHistogram().getLength() == 0) {
				continue;
			}

			double meanC1 = 0;
			double meanC2 = 0;
			double meanC3 = 0;
			for (Sample p : c.getHistogram().getPixelList()) {
				meanC1 += p.getC1() * p.getCount();
				meanC2 += p.getC2() * p.getCount();
				meanC3 += p.getC3() * p.getCount();
			}
			meanC1 = meanC1 / c.getHistogram().getCountOfPixels();
			meanC2 = meanC2 / c.getHistogram().getCountOfPixels();
			meanC3 = meanC3 / c.getHistogram().getCountOfPixels();
			Sample newMean = null;
			if (c.getCenter() instanceof RgbSample) {
				newMean = SampleFactory.createSample("RGB", meanC1, meanC2, meanC3, 1);
			} else if (c.getCenter() instanceof HsiSample) {
				newMean = SampleFactory.createSample("HSI", meanC1, meanC2, meanC3, 1);
			}
			if (!newMean.equals(c.getCenter())) {
				c.setCenter(newMean);
				this.finished = false;
			}
			if(stepCount == maxStepCount) {
				this.finished = true;
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
