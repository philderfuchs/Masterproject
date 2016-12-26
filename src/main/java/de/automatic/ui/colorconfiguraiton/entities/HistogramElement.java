package de.automatic.ui.colorconfiguraiton.entities;

public class HistogramElement {

	private double key;
	private int value;

	public HistogramElement(double key, int value) {
		super();
		this.key = key;
		this.value = value;
	}

	public double getKey() {
		return key;
	}

	public void setKey(double key) {
		this.key = key;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

}
