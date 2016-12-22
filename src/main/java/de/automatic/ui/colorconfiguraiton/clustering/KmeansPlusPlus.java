package de.automatic.ui.colorconfiguraiton.clustering;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import de.automatic.ui.colorconfiguraiton.entities.RgbChannels;
import de.automatic.ui.colorconfiguraiton.entities.Cluster;
import de.automatic.ui.colorconfiguraiton.entities.ClusterContainer;
import de.automatic.ui.colorconfiguraiton.entities.Histogram;
import de.automatic.ui.colorconfiguraiton.entities.RgbSample;
import de.automatic.ui.colorconfiguraiton.services.ErrorCalculationService;

public class KmeansPlusPlus extends AbstractKmeans {

	public KmeansPlusPlus(int k) {
		this.k = k;
		this.finished = false;
	}

	public ClusterContainer init(Histogram histogram) {

		this.finished = false;
		Random r = new Random();
		clusters = new ClusterContainer();
		int seedIndex = r.nextInt(histogram.getLength());
		RgbSample center = new RgbSample(histogram.get(seedIndex).get(RgbChannels.R), histogram.get(seedIndex).get(RgbChannels.G),
				histogram.get(seedIndex).get(RgbChannels.B), 1);

		// boolean foundSeed;
		// Collections.shuffle(histogram.getPixelList());

		for (int i = 0; i < k; i++) {
			// System.out.println("Init-Seed Index: " + seedIndex);

			clusters.add(new Cluster(new Histogram(), center));
			this.reassignPixelsToCluster(histogram, clusters);

			center = new RgbSample(this.chooseNewSeedApache(clusters));

		}

		reassignPixelsToCluster(histogram, clusters);

		return clusters;
	}

	private RgbSample chooseNewSeedApache(ClusterContainer clusters) {
		List<Pair<RgbSample, Double>> itemWeights = new ArrayList<Pair<RgbSample, Double>>();
		for (Cluster c : clusters) {
			for (RgbSample p : c.getHistogram().getPixelList()) {
				itemWeights
						.add(new Pair<RgbSample, Double>(p, ErrorCalculationService.getSquaredDistance(p, c.getCenter())));
			}
		}

		return new EnumeratedDistribution<RgbSample>(itemWeights).sample();
	}

	private RgbSample chooseNewSeedOwn(ClusterContainer clusters) {
		Random r = new Random();
		double error = clusters.getError();
		while (true) {
			for (Cluster c : clusters) {
				for (RgbSample p : c.getHistogram().getPixelList()) {
					double prob = ErrorCalculationService.getSquaredDistance(p, c.getCenter()) / error;
					double random = r.nextDouble();
					if (random <= prob) {
						return p;
					}
				}
			}
		}
	}
}
