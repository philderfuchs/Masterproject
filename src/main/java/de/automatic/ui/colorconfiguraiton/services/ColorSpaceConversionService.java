package de.automatic.ui.colorconfiguraiton.services;

import de.automatic.ui.colorconfiguraiton.entities.HsiSample;

public class ColorSpaceConversionService {

	public static HsiSample toHsi(double r, double g, double b, int count) {
		r /= 255;
		g /= 255;
		b /= 255;

		double max = Math.max(Math.max(r, g), b);
		double min = Math.min(Math.min(r, g), b);
		double c = max - min;

		double h = 0;
		double s = 0;
		
		double i = (r + g + b) / 3.0;
		if (c != 0) {
			if (max == r) {
				h = 60.0 * (((g - b) / c) % 6);
			} else if (max == g) {
				h = 60.0 * ((b - r) / c + 2);
			} else if (max == b) {
				h = 60.0 * ((r - g) / c + 4);
			}
			s = i == 0 ? 0 : 1.0 - min / i;
		}

		// double s = Math.sqrt(Math.pow(r - i, 2) + Math.pow(g - i, 2) +
		// Math.pow(b - i, 2));
		// double h = Math.toDegrees(Math.acos(((g - i) - (b - i)) / (s *
		// Math.sqrt(2.0))));

		return new HsiSample(h, s, i, count);
	}

}
