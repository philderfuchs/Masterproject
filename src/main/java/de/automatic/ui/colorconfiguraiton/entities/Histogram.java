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

	public Histogram(Histogram histo) {
		super(histo);
		this.bins = histo.getBins();
		this.channel = histo.getChannel();
		this.totalCount = histo.getTotalCount();
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

	@Override
	public HistogramElement set(int index, HistogramElement e) {
		HistogramElement old = super.set(index, e);
		this.totalCount -= old.getValue();
		this.totalCount += e.getValue();
		return old;
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

	public Channels getChannel() {
		return channel;
	}

	public void setChannel(Channels channel) {
		this.channel = channel;
	}

}
