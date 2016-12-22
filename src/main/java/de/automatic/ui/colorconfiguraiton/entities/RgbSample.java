package de.automatic.ui.colorconfiguraiton.entities;

public class RgbSample implements Sample {
	private double r;
	private double g;
	private double b;
	private int count;

	public RgbSample(int r, int g, int b, int count) {

		this.r = r;
		this.g = g;
		this.b = b;
		this.count = count;
	}

	public RgbSample(Sample p) {

		this.r = p.get(Channels.C1);
		this.g = p.get(Channels.C2);
		this.b = p.get(Channels.C3);
		this.count = p.getCount();
	}

	public double get(Channels c) {
		switch (c) {
		case C1:
			return this.r;
		case C2:
			return this.g;
		case C3:
			return this.b;
		default:
			break;
		}
		return 0;
	}

	public boolean sameAs(Sample p) {
		return p.getC1() == this.r && p.getC2() == this.g && p.getC3() == this.b;
	}

	public double getC1() {
		return r;
	}

	public void setC1(int r) {
		this.r = r;
	}

	public double getC2() {
		return g;
	}

	public void setC2(int g) {
		this.g = g;
	}

	public double getC3() {
		return b;
	}

	public void setC3(int b) {
		this.b = b;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
