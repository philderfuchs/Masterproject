package de.automatic.ui.colorconfiguraiton.entities;

import java.text.DecimalFormat;

public class HsiSample implements Sample {

	private double h;
	private double s;
	private double i;
	private int count;

	@Override
	public boolean equals(Object o) {
		DecimalFormat df = new DecimalFormat("#.###");
		HsiSample other = (HsiSample) o;
		return df.format(h).equals(df.format(other.getC1())) && df.format(s).equals(df.format(other.getC2()))
				&& df.format(i).equals(df.format(other.getC3()));
	}

	public String toString() {
		return h + " | " + s + " | " + i;
	}

	/**
	 * 
	 * @param h
	 *            unnormalized [0, ..., 360]
	 * @param s
	 *            normalized [0, ..., 1]
	 * @param i
	 *            normalize [0, ..., 1]
	 * @param count
	 */
	public HsiSample(double h, double s, double i, int count) {
		this.h = Math.min(Math.max(h, 0), 360);
		this.s = Math.min(Math.max(s, 0), 1);
		this.i = Math.min(Math.max(i, 0), 1);
		this.count = count;
	}

	public HsiSample(HsiSample p) {
		this.h = p.get(Channels.C1);
		this.s = p.get(Channels.C2);
		this.i = p.get(Channels.C3);
		this.count = p.getCount();
	}

	public double get(Channels c) {
		switch (c) {
		case C1:
			return this.h;
		case C2:
			return this.s;
		case C3:
			return this.i;
		default:
			break;
		}
		return 0;
	}

	public boolean sameAs(Sample p) {
		return p.getC1() == this.h && p.getC2() == this.s && p.getC3() == this.i;
	}

	public double getC1() {
		return h;
	}

	public void setC1(double r) {
		this.h = r;
	}

	public double getC2() {
		return s;
	}

	public void setC2(double g) {
		this.s = g;
	}

	public double getC3() {
		return i;
	}

	public void setC3(double b) {
		this.i = b;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public double getC1Normalized() {
		return h / 360.0;
	}

	@Override
	public double getC2Normalized() {
		return s;
	}

	@Override
	public double getC3Normalized() {
		return i;
	}

}
