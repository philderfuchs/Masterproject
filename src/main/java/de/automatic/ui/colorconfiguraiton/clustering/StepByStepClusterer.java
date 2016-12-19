package de.automatic.ui.colorconfiguraiton.clustering;

import java.util.ArrayList;

import de.automatic.ui.colorconfiguraiton.entities.Cluster;
import de.automatic.ui.colorconfiguraiton.entities.ClusterContainer;
import de.automatic.ui.colorconfiguraiton.entities.Histogram;

public interface StepByStepClusterer {

	public ClusterContainer init(Histogram histogram);

	public ClusterContainer step(Histogram histogram);
	
}
