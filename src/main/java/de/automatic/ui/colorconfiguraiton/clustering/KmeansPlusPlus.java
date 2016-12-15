package de.automatic.ui.colorconfiguraiton.clustering;

import java.util.ArrayList;
import java.util.Random;

import de.automatic.ui.colorconfiguraiton.entities.Channel;
import de.automatic.ui.colorconfiguraiton.entities.Cluster;
import de.automatic.ui.colorconfiguraiton.entities.Histogram;
import de.automatic.ui.colorconfiguraiton.entities.Pixel;

public class KmeansPlusPlus extends AbstractKmeans {

	public KmeansPlusPlus(int k) {
		this.k = k;
		this.finished = false;
	}

	public ArrayList<Cluster> init(Histogram histogram) {

		this.finished = false;
		Random r = new Random();
		clusters = new ArrayList<Cluster>();
		double accSquaredDistances;
		int seedIndex = r.nextInt(histogram.getLength());
		Pixel center = new Pixel(histogram.get(seedIndex).get(Channel.R), histogram.get(seedIndex).get(Channel.G),
				histogram.get(seedIndex).get(Channel.B), 1);

		boolean foundSeed;

		for (int i = 0; i < k; i++) {
			// System.out.println("Init-Seed Index: " + seedIndex);

			clusters.add(new Cluster(new Histogram(), center));
			this.reassignPixelsToCluster(histogram, clusters);

			accSquaredDistances = 0;
			for (Cluster c : clusters) {
				accSquaredDistances += c.getSquaredDistance();
			}
			
			foundSeed = false;
			outer: while (!foundSeed) {
				for (Cluster c : clusters) {
					for (Pixel p : c.getHistogram().getPixelList()) {
						double prob = this.getSquaredDistance(p, c.getCenter()) / accSquaredDistances;
						if (r.nextDouble() <= prob) {
							System.out.println("chosen");
							center = new Pixel(p.get(Channel.R), p.get(Channel.G), p.get(Channel.B), 1);
							foundSeed = true;
							break outer;
						}
					}
				}
			}

		}

		reassignPixelsToCluster(histogram, clusters);

		return clusters;
	}
}
