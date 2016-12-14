package de.automatic.ui.colorconfiguraiton.services;

import java.util.ArrayList;
import java.util.HashSet;

import de.automatic.ui.colorconfiguraiton.entities.Cluster;
import de.automatic.ui.colorconfiguraiton.entities.Pixel;

public class ClusterListConversionService {
	
	public static HashSet<Pixel> convertToHashSet(ArrayList<Cluster> clusters){
		HashSet<Pixel> pixelSet = new HashSet<>();
		for (Cluster c : clusters) {
			pixelSet.add(c.getCenter());
		}
		return pixelSet;
	}
	
}
