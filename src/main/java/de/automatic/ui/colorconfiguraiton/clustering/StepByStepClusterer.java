package de.automatic.ui.colorconfiguraiton.clustering;

import java.util.ArrayList;

import de.automatic.ui.colorconfiguraiton.entities.Cluster;
import de.automatic.ui.colorconfiguraiton.entities.Histogram;

public interface StepByStepClusterer {

	public ArrayList<Cluster> init(Histogram histogram);

	public ArrayList<Cluster> step(Histogram histogram);
	
}
