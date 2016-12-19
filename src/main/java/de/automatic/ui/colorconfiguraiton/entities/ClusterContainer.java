package de.automatic.ui.colorconfiguraiton.entities;

import java.util.ArrayList;

public class ClusterContainer extends ArrayList<Cluster> {

	private double error;

	public ClusterContainer() {
		super();

		error = 0;
		// TODO Auto-generated constructor stub
	}

	public double getError() {
		double error = 0;
		for (Cluster c : this) {
			error += c.getError();
		}
		return error;
	}
	
	public double getMaxDistance() {
		for (Cluster c : this) {
			for(Pixel p: c.getHistogram().getPixelList()) {
				
			}
		}
		return 0;
	}

}
