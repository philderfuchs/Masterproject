package de.automatic.ui.colorconfiguraiton.entities;

import de.automatic.ui.colorconfiguraiton.services.ErrorCalculationService;

public class Cluster implements Comparable<Cluster> {

	private Histogram histo;
	private Pixel center;
		
	public Cluster(Histogram histo, Pixel center) {
		super();
		this.histo = histo;
		this.center = center;
	}
	
	public Histogram getHistogram() {
		return histo;
	}
	
	public double getError(){
		double error = 0;
		for(Pixel p : histo.getPixelList()) {
			error += ErrorCalculationService.getSquaredDistance(p, center);
		}
		return error;
	}
	
	public void setHistogram(Histogram histo) {
		this.histo = histo;
	}
	
	public Pixel getCenter() {
		return center;
	}
	
	public void setCenter(Pixel center) {
		this.center = center;
	}

	@Override
	public int compareTo(Cluster o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
