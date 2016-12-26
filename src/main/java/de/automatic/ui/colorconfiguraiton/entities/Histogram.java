package de.automatic.ui.colorconfiguraiton.entities;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Collections;

public class Histogram {

	private ArrayList<Sample> samples;

	public Histogram() {
		samples = new ArrayList<Sample>();
	}

	public Histogram(ArrayList<Sample> histogram) {
		this.samples = histogram;
	}

	public void sort(Channels c) {
		switch (c) {
		case C1:
			Collections.sort(samples, new Comparator<Sample>() {
				public int compare(Sample p1, Sample p2) {
					if (p1.getC1() > p2.getC1()) {
						return 1;
					} else if (p1.getC1() == p2.getC1()) {
						return 0;
					} else {
						return -1;
					}
				}
			});
			break;
		case C2:
			Collections.sort(samples, new Comparator<Sample>() {
				public int compare(Sample p1, Sample p2) {
					if (p1.getC2() > p2.getC2()) {
						return 1;
					} else if (p1.getC2() == p2.getC2()) {
						return 0;
					} else {
						return -1;
					}
				}
			});
			break;
		case C3:
			Collections.sort(samples, new Comparator<Sample>() {
				public int compare(Sample p1, Sample p2) {
					if (p1.getC3() > p2.getC3()) {
						return 1;
					} else if (p1.getC3() == p2.getC3()) {
						return 0;
					} else {
						return -1;
					}
				}
			});
			break;
		default:
			break;
		}
	}

	public void add(Sample p) {
		this.samples.add(p);
	}

	public int getLength() {
		return samples.size();
	}

	public int getCountOfPixels() {
		int count = 0;
		for (Sample p : samples) {
			count += p.getCount();
		}
		return count;
	}

	public Sample get(int index) {
		return samples.get(index);
	}

	public ArrayList<Sample> getSamples() {
		return samples;
	}

	public void setHistogram(ArrayList<Sample> histogram) {
		this.samples = histogram;

	}

	public void clear() {
		this.samples.clear();
	}

}
