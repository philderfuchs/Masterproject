package de.automatic.ui.colorconfiguraiton.entities;

public class HistogramElement {

	private double key;
	private double value;

	public HistogramElement(double key, double value) {
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

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

}
