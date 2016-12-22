package de.automatic.ui.colorconfiguraiton.entities;

public class RgbSample {
	private int r;
	private int g;
	private int b;
	private int count;

	public RgbSample(int r, int g, int b, int count) {

		this.r = r;
		this.g = g;
		this.b = b;
		this.count = count;
	}

	public RgbSample(RgbSample p) {

		this.r = p.get(RgbChannels.R);
		this.g = p.get(RgbChannels.G);
		this.b = p.get(RgbChannels.B);
		this.count = p.count;
	}

	public int get(RgbChannels c) {
		switch (c) {
		case R:
			return this.r;
		case G:
			return this.g;
		case B:
			return this.b;
		default:
			break;
		}
		return 0;
	}

	public boolean sameAs(RgbSample p) {
		return p.getC1() == this.r && p.getC2() == this.g && p.getC3() == this.b;
	}

	public int getC1() {
		return r;
	}

	public void setC1(int r) {
		this.r = r;
	}

	public int getC2() {
		return g;
	}

	public void setC2(int g) {
		this.g = g;
	}

	public int getC3() {
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
