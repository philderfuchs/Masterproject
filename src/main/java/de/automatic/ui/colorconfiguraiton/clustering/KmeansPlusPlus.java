package de.automatic.ui.colorconfiguraiton.clustering;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import de.automatic.ui.colorconfiguraiton.entities.Channels;
import de.automatic.ui.colorconfiguraiton.entities.Cluster;
import de.automatic.ui.colorconfiguraiton.entities.ClusterContainer;
import de.automatic.ui.colorconfiguraiton.entities.SampleList;
import de.automatic.ui.colorconfiguraiton.entities.RgbSample;
import de.automatic.ui.colorconfiguraiton.entities.Sample;
import de.automatic.ui.colorconfiguraiton.entities.SampleFactory;
import de.automatic.ui.colorconfiguraiton.services.ErrorCalculationService;

public class KmeansPlusPlus extends AbstractKmeans {

	public KmeansPlusPlus(int k) {
		this.k = k;
		this.finished = false;
	}

	public ClusterContainer init(SampleList histogram) {
		this.finished = false;
		Random r = new Random();
		clusters = new ClusterContainer();
		int seedIndex = r.nextInt(histogram.size());
		Sample center = SampleFactory.createSample((histogram.get(seedIndex)));

		// boolean foundSeed;
		// Collections.shuffle(histogram.getPixelList());

		for (int i = 0; i < k; i++) {
			// System.out.println("Init-Seed Index: " + seedIndex);

			clusters.add(new Cluster(new SampleList(), center));
			this.reassignPixelsToCluster(histogram, clusters);

			center = SampleFactory.createSample(this.chooseNewSeedApache(clusters));
		}

		reassignPixelsToCluster(histogram, clusters);

		return clusters;
	}

	private Sample chooseNewSeedApache(ClusterContainer clusters) {
		List<Pair<Sample, Double>> itemWeights = new ArrayList<Pair<Sample, Double>>();
		for (Cluster c : clusters) {
			for (Sample p : c.getSampleList()) {
				itemWeights
						.add(new Pair<Sample, Double>(p, ErrorCalculationService.getSquaredDistance(p, c.getCenter())));
			}
		}

		return new EnumeratedDistribution<Sample>(itemWeights).sample();
	}

	private Sample chooseNewSeedOwn(ClusterContainer clusters) {
		Random r = new Random();
		double error = clusters.getError();
		while (true) {
			for (Cluster c : clusters) {
				for (Sample p : c.getSampleList()) {
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
