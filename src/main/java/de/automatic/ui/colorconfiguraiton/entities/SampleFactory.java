package de.automatic.ui.colorconfiguraiton.entities;

public class SampleFactory {

	public static Sample createSample(Sample s) {
		if (s instanceof RgbSample) {
			return new RgbSample((RgbSample) s);
		} else if (s instanceof HsiSample) {
			return new HsiSample((HsiSample) s);
		} else {
			return null;
		}
	}
	
}
