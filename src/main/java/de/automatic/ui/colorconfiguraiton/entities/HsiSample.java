package de.automatic.ui.colorconfiguraiton.entities;

public class HsiSample implements Sample {

	private double h;
	private double s;
	private double i;
	private int count;

	public HsiSample(double r, double g, double b, int count) {
		this.h = r;
		this.s = g;
		this.i = b;
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

}
