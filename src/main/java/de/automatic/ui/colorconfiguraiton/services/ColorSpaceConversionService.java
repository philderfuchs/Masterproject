package de.automatic.ui.colorconfiguraiton.services;

import de.automatic.ui.colorconfiguraiton.entities.HsiSample;
import de.automatic.ui.colorconfiguraiton.entities.RgbSample;
import de.automatic.ui.colorconfiguraiton.entities.Sample;

public class ColorSpaceConversionService {

	public static double getX(Sample s) {
		if (s instanceof RgbSample) {
			return s.getC1Normalized();
		} else if (s instanceof HsiSample) {
			if (s.getC1() == 0 || s.getC1() == 180) {
				return s.getC2();
			} else if (s.getC1() == 90.0 || s.getC1() == 270.0) {
				return 0.0;
			} else {
				return Math.cos(Math.toRadians(s.getC1())) * s.getC2();
			}
		}
		return 0.0;
	}

	public static double getY(Sample s) {
		if (s instanceof RgbSample) {
			return s.getC2Normalized();
		} else {
			if (s.getC1() == 0.0 || s.getC1() == 180.0) {
				return 0.0;
			} else if (s.getC1() == 90.0 || s.getC1() == 270.0) {
				return s.getC2();
			} else {
				return Math.sin(Math.toRadians(s.getC1())) * s.getC2();
			}
		}
	}

	public static double getZ(Sample s) {
		if (s instanceof RgbSample) {
			return s.getC3Normalized();
		} else {
			return s.getC3Normalized();
		}
	}

	public static RgbSample toRgb(Sample p) {
		if (p instanceof RgbSample) {
			return (RgbSample) p;
		} else {
			return toRgb(p.getC1(), p.getC2(), p.getC3(), p.getCount());
		}
	}

	/**
	 * 
	 * @param h
	 *            unnormalized [0, ..., 360]
	 * @param s
	 *            normalized, as always [0, ..., 1]
	 * @param i
	 *            normalized, as always [0, ..., 1]
	 * @param count
	 * @return
	 */

	public static RgbSample toRgb(double h, double s, double i, int count) {
		if (s == 0.0) {
			return new RgbSample(i, i, i, count);
		}

		double hr = Math.toRadians(h);
		double x = i * (1.0 - s);

		double r, g, b;

		if (hr <= 2.0 / 3.0 * Math.PI) {
			double y = i * (1.0 + s * Math.cos(hr) / Math.cos(Math.PI / 3.0 - hr));
			double z = 3.0 * i - (x + y);
			r = y * 255.0;
			g = z * 255.0;
			b = x * 255.0;
		} else if (hr <= 4.0 / 3.0 * Math.PI) {
			hr -= (2.0 / 3.0) * Math.PI;
			double y = i * (1.0 + s * Math.cos(hr) / Math.cos(Math.PI / 3.0 - hr));
			double z = 3.0 * i - (x + y);
			r = x * 255.0;
			g = y * 255.0;
			b = z * 255.0;
		} else {
			hr -= (4.0 / 3.0) * Math.PI;
			double y = i * (1.0 + s * Math.cos(hr) / Math.cos(Math.PI / 3.0 - hr));
			double z = 3.0 * i - (x + y);
			r = z * 255.0;
			g = x * 255.0;
			b = y * 255.0;
		}
		return new RgbSample(r, g, b, count);

	}

	/**
	 * 
	 * @param r
	 *            unnormalized [0, ... ,255]
	 * @param g
	 *            unnormalized [0, ... ,255]
	 * @param b
	 *            unnormalized [0, ... ,255]
	 * @param count
	 * @return
	 */
	public static HsiSample toHsi(double r, double g, double b, int count) {

		r /= 255.0;
		g /= 255.0;
		b /= 255.0;

		double max = Math.max(Math.max(r, g), b);
		double min = Math.min(Math.min(r, g), b);
		double c = max - min;

		double i = (r + g + b) / 3.0;
		// double s = i == 0 ? 0 : 1.0 - min / i;
		double s = i == 0 ? 0 : Math.sqrt(Math.pow(r - i, 2) + Math.pow(g - i, 2) + Math.pow(b - i, 2));

		double h = 0;
		if (c != 0) {
			h = Math.toDegrees(
					Math.acos((0.5 * ((r - g) + (r - b))) / Math.sqrt(Math.pow((r - g), 2) + (r - b) * (g - b))));
			// h = Math.toDegrees(Math.acos(((2 * r - g - b) / (2 *
			// Math.sqrt(Math.pow(r - g, 2) + (r - b) * (g - b))))));
			if (b > g)
				h = 360.0 - h;
		}

		return new HsiSample(h, s, i, count);
	}
}
