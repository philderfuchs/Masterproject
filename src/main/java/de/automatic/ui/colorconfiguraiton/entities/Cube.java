package de.automatic.ui.colorconfiguraiton.entities;

public class Cube extends Cluster implements Comparable<Cluster> {

	private CubeDimensions cubeDimensions;

	public Cube(Histogram histo) {
		super(histo, new CubeDimensions(histo).getCenter());
		this.cubeDimensions = new CubeDimensions(histo);
	}

	public CubeDimensions getCubeDimensions() {
		return cubeDimensions;
	}

	public void setCubeDimensions(CubeDimensions cubeDimensions) {
		this.cubeDimensions = cubeDimensions;
	}

	public int compareTo(Cube c) {
		if (this.cubeDimensions.getSize() < c.getCubeDimensions().getSize()) {
			return 1;
		} else if (this.cubeDimensions.getSize() == c.getCubeDimensions().getSize()) {
			return 0;
		} else {
			return -1;
		}
	}
	
	public Channels getLongestDistance () {
		return this.getCubeDimensions().getLongestDistance();
	}
	
	public int getRdiff(){
		return this.getCubeDimensions().getRdiff();
	}
	
	public int getGdiff(){
		return this.getCubeDimensions().getGdiff();
	}

	public int getBdiff(){
		return this.getCubeDimensions().getBdiff();
	}

}
