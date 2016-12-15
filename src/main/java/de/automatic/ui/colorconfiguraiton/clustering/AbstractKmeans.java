package de.automatic.ui.colorconfiguraiton.clustering;

import java.util.ArrayList;

import de.automatic.ui.colorconfiguraiton.entities.*;

public abstract class AbstractKmeans implements StepByStepClusterer, FinishingClusterer {

	protected int k;
	protected ArrayList<Cluster> clusters;
	protected boolean finished;

	@Override
	public ArrayList<Cluster> clusterToEnd(Histogram histogram) {
		this.init(histogram);
		
		while (!finished) {
			this.step(histogram);
		}
		return clusters;
	}

	public abstract ArrayList<Cluster> init(Histogram histogram);

	public ArrayList<Cluster> step(Histogram histogram) {

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
			for (Pixel p : c.getHistogram().getPixelList()) {
				meanR += p.getR() * p.getCount();
				meanG += p.getG() * p.getCount();
				meanB += p.getB() * p.getCount();
			}
			meanR = meanR / c.getHistogram().getCountOfPixels();
			meanG = meanG / c.getHistogram().getCountOfPixels();
			meanB = meanB / c.getHistogram().getCountOfPixels();
			Pixel newMean = new Pixel((int) meanR, (int) meanG, (int) meanB, 1);
			if (!newMean.sameAs(c.getCenter())) {
				c.setCenter(newMean);
				this.finished = false;
			}
		}

		reassignPixelsToCluster(histogram, clusters);

		return clusters;
	}

	protected void reassignPixelsToCluster(Histogram histogram, ArrayList<Cluster> clusters) {
		for (Cluster c : clusters) {
			c.getHistogram().clear();
		}
		// put each pixel in the histogram in the closest cluster
		for (Pixel p : histogram.getPixelList()) {
			double minDistance = Double.MAX_VALUE;
			Cluster closestCluster = null;
			for (Cluster c : clusters) {
				double currentDistance = getEucledianDistance(p, c.getCenter());
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

	public ArrayList<Cluster> getClusters() {
		return clusters;
	}

	public void setClusters(ArrayList<Cluster> clusters) {
		this.clusters = clusters;
	}

	protected double getEucledianDistance(Pixel p1, Pixel p2) {
		return (Math.sqrt(Math.pow(p1.getR() - p2.getR(), 2) + Math.pow(p1.getG() - p2.getG(), 2)
				+ Math.pow(p1.getB() - p2.getB(), 2)));
	}

	protected double getSquaredDistance(Pixel p1, Pixel p2) {
		return Math.sqrt(Math.pow(p1.getR() - p2.getR(), 2) + Math.pow(p1.getG() - p2.getG(), 2)
				+ Math.pow(p1.getB() - p2.getB(), 2));
	}

}
