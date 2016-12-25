package de.automatic.ui.colorconfiguraiton.clustering;

import java.util.ArrayList;

import de.automatic.ui.colorconfiguraiton.entities.*;
import de.automatic.ui.colorconfiguraiton.services.ColorSpaceConversionService;
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

			double meanX = 0;
			double meanY = 0;
			double meanZ = 0;
			for (Sample s : c.getHistogram().getPixelList()) {
				CartesianCoordinates coord = ColorSpaceConversionService.toCoordinates(s);
				meanX += coord.getX() * (double) s.getCount();
				meanY += coord.getY() * (double) s.getCount();
				meanZ += coord.getZ() * (double) s.getCount();
			}
			meanX = meanX / (double) c.getHistogram().getCountOfPixels();
			meanY = meanY / (double) c.getHistogram().getCountOfPixels();
			meanZ = meanZ / (double) c.getHistogram().getCountOfPixels();
			Sample newMean = null;
			if (c.getCenter() instanceof RgbSample) {
				newMean = ColorSpaceConversionService.toRgb(new CartesianCoordinates(meanX, meanY, meanZ), 1);
			} else if (c.getCenter() instanceof HsiSample) {
				// System.out.println("C1 " + meanC1 * 360.0);
				// System.out.println("C2 " + meanC2);
				// System.out.println("C3 " + meanC3);
				newMean = ColorSpaceConversionService.toHsi(new CartesianCoordinates(meanX, meanY, meanZ), 1);
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
