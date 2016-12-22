package de.automatic.ui.colorconfiguraiton.services;

import java.util.ArrayList;
import java.util.HashSet;

import de.automatic.ui.colorconfiguraiton.entities.Cluster;
import de.automatic.ui.colorconfiguraiton.entities.ClusterContainer;
import de.automatic.ui.colorconfiguraiton.entities.RgbPixel;

public class ClusterListConversionService {
	
	public static HashSet<RgbPixel> convertToHashSet(ClusterContainer clusters){
		HashSet<RgbPixel> pixelSet = new HashSet<>();
		for (Cluster c : clusters) {
			pixelSet.add(c.getCenter());
		}
		return pixelSet;
	}
	
}
