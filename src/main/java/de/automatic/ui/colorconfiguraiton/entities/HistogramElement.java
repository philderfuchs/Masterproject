package de.automatic.ui.colorconfiguraiton.entities;

public class HistogramElement {

	private double key;
	private double value;
	private SampleList samples;

	public HistogramElement(double key, double value) {
		super();
		this.key = key;
		this.value = value;
		samples = new SampleList();
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

	public SampleList getSamples() {
		return samples;
	}

	public void setSamples(SampleList samples) {
		this.samples = samples;
	}

}
