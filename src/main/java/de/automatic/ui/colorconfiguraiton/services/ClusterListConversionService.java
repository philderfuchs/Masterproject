package de.automatic.ui.colorconfiguraiton.services;

import java.util.ArrayList;
import java.util.HashSet;

import de.automatic.ui.colorconfiguraiton.entities.Cluster;
import de.automatic.ui.colorconfiguraiton.entities.ClusterContainer;
import de.automatic.ui.colorconfiguraiton.entities.RgbSample;
import de.automatic.ui.colorconfiguraiton.entities.Sample;

public class ClusterListConversionService {
	
	public static HashSet<Sample> convertToHashSet(ClusterContainer clusters){
		HashSet<Sample> pixelSet = new HashSet<>();
		for (Cluster c : clusters) {
			pixelSet.add(c.getCenter());
		}
		return pixelSet;
	}
	
}
