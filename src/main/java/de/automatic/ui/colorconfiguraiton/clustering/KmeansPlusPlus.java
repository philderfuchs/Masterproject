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
		
		for (int i = 0; i < k; i++) {
			int initSeedIndex = r.nextInt(histogram.getLength());
			System.out.println("Init-Seed Index: " + initSeedIndex);

			clusters.add(new Cluster(
					new Histogram(),
					new Pixel(histogram.get(initSeedIndex).get(Channel.R), 
							histogram.get(initSeedIndex).get(Channel.G), 
							histogram.get(initSeedIndex).get(Channel.B),
							1)
						)
					);			
		}


		reassignPixelsToCluster(histogram, clusters);

		return clusters;
	}
}
