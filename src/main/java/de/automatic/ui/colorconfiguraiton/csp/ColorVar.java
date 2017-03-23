package de.automatic.ui.colorconfiguraiton.csp;

public class ColorVar {

	private int hueGroupSize;

	private int hueGroup;

	/*
	 * 0..1
	 */
	private double relativeHueGroupSize;

	/*
	 * 0..1
	 */
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

	private double cieL;

	/*
	 * 0..128
	 */
	private double lchC;

	private double lchH;

	private double cieA;

	private double cieB;

	// Pattern & Lübbe 2010
	public double getSaturation() {
		return Math.sqrt(Math.pow(cieA, 2) + Math.pow(cieB, 2))
				/ Math.sqrt(Math.pow(cieA, 2) + Math.pow(cieB, 2) + Math.pow(cieL, 2));
	}

	public double getRelativeChroma() {
		return lchC / 128.0;
	}

	public double getCieA() {
		return cieA;
	}

	public void setCieA(double cieA) {
		this.cieA = cieA;
	}

	public double getCieB() {
		return cieB;
	}

	public void setCieB(double cieB) {
		this.cieB = cieB;
	}

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

	public int getHueGroup() {
		return hueGroup;
	}

	public void setHueGroup(int hueGroup) {
		this.hueGroup = hueGroup;
	}

	public double getRelativeHueGroupSize() {
		return relativeHueGroupSize;
	}

	public void setRelativeHueGroupSize(double relativeHueGroupSize) {
		this.relativeHueGroupSize = relativeHueGroupSize;
	}

	public double getCieL() {
		return cieL;
	}

	public void setCieL(double cieL) {
		this.cieL = cieL;
	}

	public double getLchC() {
		return lchC;
	}

	public void setLchC(double lchC) {
		this.lchC = lchC;
	}

	public double getLchH() {
		return lchH;
	}

	public void setLchH(double lchH) {
		this.lchH = lchH;
	}

	public int getHueGroupSize() {
		return hueGroupSize;
	}

	public void setHueGroupSize(int hueGroupSize) {
		this.hueGroupSize = hueGroupSize;
	}

}
