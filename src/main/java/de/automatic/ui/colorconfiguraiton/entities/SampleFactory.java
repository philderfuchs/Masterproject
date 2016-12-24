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
	
	public static Sample createSample(String s, double c1, double c2, double c3, int count) {
		if (s.equals("RGB")) {
			return new RgbSample(c1, c2, c3, count);
		} else if (s.equals("HSI")) {
			return new HsiSample(c1, c2, c3, count);
		} else {
			return null;
		}
	}
	
	public static Sample createSampleFromNormalized(String s, double c1, double c2, double c3, int count) {
		if (s.equals("RGB")) {
			return new RgbSample(c1 * 255.0, c2  * 255.0, c3  * 255.0, count);
		} else if (s.equals("HSI")) {
			return new HsiSample(c1 * 360.0, c2, c3, count);
		} else {
			return null;
		}
	}
	
}
