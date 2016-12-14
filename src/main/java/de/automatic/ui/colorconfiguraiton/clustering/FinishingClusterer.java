package de.automatic.ui.colorconfiguraiton.clustering;

import java.util.ArrayList;

import de.automatic.ui.colorconfiguraiton.entities.Cluster;
import de.automatic.ui.colorconfiguraiton.entities.Histogram;

public interface FinishingClusterer {

	public ArrayList<Cluster> clusterToEnd(Histogram histogram);
	
}
