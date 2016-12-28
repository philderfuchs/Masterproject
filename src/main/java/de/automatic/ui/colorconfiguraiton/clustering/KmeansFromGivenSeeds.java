package de.automatic.ui.colorconfiguraiton.clustering;

import de.automatic.ui.colorconfiguraiton.entities.Cluster;
import de.automatic.ui.colorconfiguraiton.entities.ClusterContainer;
import de.automatic.ui.colorconfiguraiton.entities.RgbSample;
import de.automatic.ui.colorconfiguraiton.entities.Sample;
import de.automatic.ui.colorconfiguraiton.entities.SampleList;

public class KmeansFromGivenSeeds extends AbstractKmeans {

	private SampleList seeds;

	public KmeansFromGivenSeeds(SampleList seeds) {
		this.seeds = seeds;
		this.k = seeds.size();
		this.finished = false;
	}

	@Override
	public ClusterContainer init(SampleList histogram) {
		this.finished = false;
		// clusters not yet initialized
		clusters = new ClusterContainer();
		for (Sample s : seeds) {
			clusters.add(new Cluster(new SampleList(), s));
		}

		reassignPixelsToCluster(histogram, clusters);
		return clusters;
	}

}
