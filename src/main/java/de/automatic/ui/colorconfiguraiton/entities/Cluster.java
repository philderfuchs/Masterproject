package de.automatic.ui.colorconfiguraiton.entities;

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
			error += this.getSquaredDistance(p, center);
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
	
	protected double getSquaredDistance(Pixel p1, Pixel p2) {
		return Math.sqrt(Math.pow(p1.getR() - p2.getR(), 2) + Math.pow(p1.getG() - p2.getG(), 2)
				+ Math.pow(p1.getB() - p2.getB(), 2));
	}
	
}
