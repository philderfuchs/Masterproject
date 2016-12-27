package de.automatic.ui.colorconfiguraiton.entities;

import java.util.ArrayList;

public class Histogram extends ArrayList<HistogramElement> {

	private int bins;
	private int totalCount;
	private Channels channel;

	public Histogram(int bins, Channels channel) {
		super(bins);
		this.bins = bins;
		this.channel = channel;
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
	
	public boolean add(Integer i) {
		totalCount += i;
		double binRange = 1.0 / bins;
		HistogramElement e = new HistogramElement(((double) this.size() + 0.5) * binRange, i);
		return super.add(e);
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

}
