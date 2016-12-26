package de.automatic.ui.colorconfiguraiton.entities;

import java.util.ArrayList;

public class Histogram extends ArrayList<HistogramElement> {

	private int bins;

	public Histogram(int bins) {
		super();
		this.bins = bins;
	}

	public int getBins() {
		return bins;
	}

	public void setBins(int bins) {
		this.bins = bins;
	}

}
