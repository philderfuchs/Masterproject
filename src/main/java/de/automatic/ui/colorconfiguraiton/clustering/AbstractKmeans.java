package de.automatic.ui.colorconfiguraiton.clustering;

import java.util.ArrayList;

import de.automatic.ui.colorconfiguraiton.entities.*;
import de.automatic.ui.colorconfiguraiton.services.ErrorCalculationService;

public abstract class AbstractKmeans implements StepByStepClusterer, FinishingClusterer {

	protected int maxStepCount = 20;

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
			System.out.println("init from step");
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
				meanC1 += p.getC1Normalized() * (double) p.getCount();
				meanC2 += p.getC2Normalized() * (double) p.getCount();
				meanC3 += p.getC3Normalized() * (double) p.getCount();
			}
			meanC1 = meanC1 / (double) c.getHistogram().getCountOfPixels();
			meanC2 = meanC2 / (double) c.getHistogram().getCountOfPixels();
			meanC3 = meanC3 / (double) c.getHistogram().getCountOfPixels();
			Sample newMean = null;
			if (c.getCenter() instanceof RgbSample) {
				newMean = SampleFactory.createSampleFromNormalized("RGB", meanC1, meanC2, meanC3, 1);
			} else if (c.getCenter() instanceof HsiSample) {
//				System.out.println("C1 " + meanC1 * 360.0);
//				System.out.println("C2 " + meanC2);
//				System.out.println("C3 " + meanC3);
				newMean = SampleFactory.createSampleFromNormalized("HSI", meanC1, meanC2, meanC3, 1);
			}
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
