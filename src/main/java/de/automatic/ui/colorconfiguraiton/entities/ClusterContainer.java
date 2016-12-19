package de.automatic.ui.colorconfiguraiton.entities;

import java.util.ArrayList;

import de.automatic.ui.colorconfiguraiton.services.ErrorCalculationService;

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

	public double getMaxSquaredDistance() {
		double maxDistance = Double.MIN_VALUE;
		for (Cluster c : this) {
			for (Pixel p : c.getHistogram().getPixelList()) {
				double currentDistance = ErrorCalculationService.getSquaredDistance(p, c.getCenter());
				if (currentDistance > maxDistance) {
					maxDistance = currentDistance;
				}
			}
		}
		System.out.println("MaxDistance: " + maxDistance);
		return maxDistance;
	}

}
