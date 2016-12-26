package de.automatic.ui.colorconfiguraiton.clustering;

import java.util.ArrayList;

import de.automatic.ui.colorconfiguraiton.entities.Cluster;
import de.automatic.ui.colorconfiguraiton.entities.ClusterContainer;
import de.automatic.ui.colorconfiguraiton.entities.SampleList;

public interface StepByStepClusterer {

	public ClusterContainer init(SampleList histogram);

	public ClusterContainer step(SampleList histogram);
	
}
