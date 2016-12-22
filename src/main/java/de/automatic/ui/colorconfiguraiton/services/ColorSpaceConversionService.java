package de.automatic.ui.colorconfiguraiton.services;

import de.automatic.ui.colorconfiguraiton.entities.HsiSample;

public class ColorSpaceConversionService {

	public static HsiSample toHsi(double r, double b, double g, int count) {

		double i = (r + g + b) / 3.0;
		double s = Math.sqrt(Math.pow(r - i, 2) + Math.pow(g - i, 2) + Math.pow(b - i, 2));
		double h = Math.acos(((g - i) - (b - i)) / (s * Math.sqrt(2.0)));

		return new HsiSample(h, s, i, count);
	}

}
