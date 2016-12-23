package de.automatic.ui.colorconfiguraiton.entities;

import de.automatic.ui.colorconfiguraiton.services.ErrorCalculationService;

public class Cluster {

	private Histogram histo;
	private Sample center;
		
	public Cluster(Histogram histo, Sample center) {
		super();
		this.histo = histo;
		this.center = center;
	}
	
	public Histogram getHistogram() {
		return histo;
	}
	
	public double getError(){
		double error = 0;
		for(Sample p : histo.getPixelList()) {
			error += ErrorCalculationService.getSquaredDistance(p, center);
		}
		return error;
	}
	
	public void setHistogram(Histogram histo) {
		this.histo = histo;
	}
	
	public Sample getCenter() {
		return center;
	}
	
	public void setCenter(Sample center) {
		this.center = center;
	}
	
}
