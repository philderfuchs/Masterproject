package de.automatic.ui.colorconfiguraiton.entities;

public class RgbSample implements Sample {
	private double r;
	private double g;
	private double b;
	private int count;
	
	public String toString() {
		return r + " | " + g + " | " + b;
	}

	public RgbSample(double r, double g, double b, int count) {
		this.r = Math.min(Math.max(r, 0), 255);
		this.g = Math.min(Math.max(g, 0), 255);
		this.b = Math.min(Math.max(b, 0), 255);
		this.count = count;
	}

	public RgbSample(RgbSample p) {

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

	@Override
	public boolean equals(Object o) {
		RgbSample other = (RgbSample) o;
		return other.getC1() == this.r && other.getC2() == this.g && other.getC3() == this.b;
	}

	public double getC1() {
		return r;
	}

	public void setC1(double r) {
		this.r = r;
	}

	public double getC2() {
		return g;
	}

	public void setC2(double g) {
		this.g = g;
	}

	public double getC3() {
		return b;
	}

	public void setC3(double b) {
		this.b = b;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
