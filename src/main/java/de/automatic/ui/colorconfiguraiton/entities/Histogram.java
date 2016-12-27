package de.automatic.ui.colorconfiguraiton.entities;

import java.util.ArrayList;

public class Histogram extends ArrayList<HistogramElement> {

	private int bins;
	private int totalCount;

	public Histogram(int bins) {
		super(bins);
		this.bins = bins;
		this.totalCount = 0;
	}

	public int getBins() {
		return bins;
	}

	public void setBins(int bins) {
		this.bins = bins;
	}

	@Override
	public boolean add(HistogramElement e) {
		totalCount += e.getValue();
		return super.add(e);
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
