package de.automatic.ui.colorconfiguraiton.csp;

public class ColorVar {
	
	private double relativeWeight;

	/*
	 * 0..360
	 */
	private double h;
	
	/*
	 * 0..1
	 */
	private double s;
	
	/*
	 * 0..1
	 */
	private double i;
	
	/*
	 * 0..255
	 */
	private int r;
	
	/*
	 * 0..255
	 */
	private int g;
	
	/*
	 * 0..255
	 */
	private int b;

	public double getH() {
		return h;
	}

	public void setH(double h) {
		this.h = h;
	}

	public double getS() {
		return s;
	}

	public void setS(double s) {
		this.s = s;
	}

	public double getI() {
		return i;
	}

	public void setI(double i) {
		this.i = i;
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

	public double getRelativeWeight() {
		return relativeWeight;
	}

	public void setRelativeWeight(double relativeWeight) {
		this.relativeWeight = relativeWeight;
	}
	
}
