package de.automatic.ui.colorconfiguraiton.clustering;

import java.util.ArrayList;

import de.automatic.ui.colorconfiguraiton.entities.*;
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

			double meanX = 0;
			double meanY = 0;
			double meanZ = 0;
			for (Sample s : c.getSampleList()) {
				CartesianCoordinates coord = ConversionService.toCoordinates(s);
				meanX += coord.getX() * (double) s.getCount();
				meanY += coord.getY() * (double) s.getCount();
				meanZ += coord.getZ() * (double) s.getCount();
				// meanX += coord.getX() / c.getHistogram().getLength();
				// meanY += coord.getY() / c.getHistogram().getLength();
				// meanZ += coord.getZ() / c.getHistogram().getLength();
			}
			meanX = meanX / (double) c.getSampleList().getCountOfPixels();
			meanY = meanY / (double) c.getSampleList().getCountOfPixels();
			meanZ = meanZ / (double) c.getSampleList().getCountOfPixels();
			Sample newMean = null;
			if (c.getCenter() instanceof RgbSample) {
				newMean = ConversionService.toRgb(new CartesianCoordinates(meanX, meanY, meanZ), 1);
			} else if (c.getCenter() instanceof HsiSample) {
				// System.out.println("C1 " + meanC1 * 360.0);
				// System.out.println("C2 " + meanC2);
				// System.out.println("C3 " + meanC3);
				newMean = ConversionService.toHsi(new CartesianCoordinates(meanX, meanY, meanZ), 1);
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
