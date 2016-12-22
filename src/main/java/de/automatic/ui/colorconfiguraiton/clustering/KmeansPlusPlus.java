package de.automatic.ui.colorconfiguraiton.clustering;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import de.automatic.ui.colorconfiguraiton.entities.Channel;
import de.automatic.ui.colorconfiguraiton.entities.Cluster;
import de.automatic.ui.colorconfiguraiton.entities.ClusterContainer;
import de.automatic.ui.colorconfiguraiton.entities.Histogram;
import de.automatic.ui.colorconfiguraiton.entities.RgbPixel;
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
		RgbPixel center = new RgbPixel(histogram.get(seedIndex).get(Channel.R), histogram.get(seedIndex).get(Channel.G),
				histogram.get(seedIndex).get(Channel.B), 1);

		// boolean foundSeed;
		// Collections.shuffle(histogram.getPixelList());

		for (int i = 0; i < k; i++) {
			// System.out.println("Init-Seed Index: " + seedIndex);

			clusters.add(new Cluster(new Histogram(), center));
			this.reassignPixelsToCluster(histogram, clusters);

			center = new RgbPixel(this.chooseNewSeedApache(clusters));

		}

		reassignPixelsToCluster(histogram, clusters);

		return clusters;
	}

	private RgbPixel chooseNewSeedApache(ClusterContainer clusters) {
		List<Pair<RgbPixel, Double>> itemWeights = new ArrayList<Pair<RgbPixel, Double>>();
		for (Cluster c : clusters) {
			for (RgbPixel p : c.getHistogram().getPixelList()) {
				itemWeights
						.add(new Pair<RgbPixel, Double>(p, ErrorCalculationService.getSquaredDistance(p, c.getCenter())));
			}
		}

		return new EnumeratedDistribution<RgbPixel>(itemWeights).sample();
	}

	private RgbPixel chooseNewSeedOwn(ClusterContainer clusters) {
		Random r = new Random();
		double error = clusters.getError();
		while (true) {
			for (Cluster c : clusters) {
				for (RgbPixel p : c.getHistogram().getPixelList()) {
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
