package de.automatic.ui.colorconfiguraiton.entities;

import java.awt.Color;

public class Pixel {
	private int r;
	private int g;
	private int b;
	private int count;
	private int rgb;

	public Pixel(int r, int g, int b, int rgb, int count) {

		this.r = r;
		this.g = g;
		this.b = b;
		this.count = count;
		this.rgb = rgb;
	}

	public Pixel(int r, int g, int b, int count) {

		this.r = r;
		this.g = g;
		this.b = b;
		this.count = count;
		this.rgb = ((255 & 0x0ff) << 24) | ((r & 0x0ff) << 16) | ((g & 0x0ff) << 8) | (b & 0x0ff);
	}

	public Pixel(Pixel p) {

		this.r = p.get(Channel.R);
		this.g = p.get(Channel.G);
		this.b = p.get(Channel.B);
		this.count = p.count;
		this.rgb = p.getRgb();
	}

	public int get(Channel c) {
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

	public boolean sameAs(Pixel p) {
		return p.getR() == this.r && p.getG() == this.g && p.getB() == this.b;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getRgb() {
		return rgb;
	}

	public void setRgb(int rgb) {
		this.rgb = rgb;
	}

}
