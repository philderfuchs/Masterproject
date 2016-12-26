package de.automatic.ui.colorconfiguraiton.entities;

import de.automatic.ui.colorconfiguraiton.services.ErrorCalculationService;

public class Cluster {

	private SampleList samples;
	private Sample center;
		
	public Cluster(SampleList histo, Sample center) {
		super();
		this.samples = histo;
		this.center = center;
	}
	
	public SampleList getSampleList() {
		return samples;
	}
	
	public double getError(){
		double error = 0;
		for(Sample p : samples) {
			error += ErrorCalculationService.getSquaredDistance(p, center);
		}
		return error;
	}
	
	public void setSampleList(SampleList sample) {
		this.samples = sample;
	}
	
	public Sample getCenter() {
		return center;
	}
	
	public void setCenter(Sample center) {
		this.center = center;
	}
	
}
