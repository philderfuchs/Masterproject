package de.automatic.ui.colorconfiguraiton.services;

import de.automatic.ui.colorconfiguraiton.entities.HsiSample;
import de.automatic.ui.colorconfiguraiton.entities.RgbSample;
import de.automatic.ui.colorconfiguraiton.entities.Sample;

public class ColorSpaceConversionService {

	public static double getX(Sample s) {
		if (s instanceof RgbSample) {
			return s.getC1() / 255.0;
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
			return s.getC2() / 255.0;
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
			return s.getC3() / 255.0;
		} else {
			return s.getC3();
		}
	}

	public static RgbSample toRgb(Sample p) {
		if (p instanceof RgbSample) {
			return (RgbSample) p;
		} else {
			return toRgb(p.getC1(), p.getC2(), p.getC3(), p.getCount());
		}
	}

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

	public static HsiSample toHsi(double r, double g, double b, int count) {
		r /= 255.0;
		g /= 255.0;
		b /= 255.0;

		double max = Math.max(Math.max(r, g), b);
		double min = Math.min(Math.min(r, g), b);
		double c = max - min;

		double i = (r + g + b) / 3.0;
		double s = i == 0 ? 0 : 1.0 - min / i;

		double h = 0;
		if (c != 0) {
			h = Math.toDegrees(
					Math.acos((0.5 * ((r - g) + (r - b))) / Math.sqrt(Math.pow((r - g), 2) + (r - b) * (g - b))));
			if (b / i > g / i)
				h = 360.0 - h;
		}

		return new HsiSample(h, s, i, count);
	}

	// public static HsiSample toHsi(double r, double g, double b, int count) {
	// r /= 255.0;
	// g /= 255.0;
	// b /= 255.0;
	//
	// double max = Math.max(Math.max(r, g), b);
	// double min = Math.min(Math.min(r, g), b);
	// double c = max - min;
	//
	// double h = 0;
	// double s = 0;
	//
	// double i = (r + g + b) / 3.0;
	// if (c != 0) {
	// if (max == r) {
	// h = 60.0 * (((g - b) / c) % 6);
	// } else if (max == g) {
	// h = 60.0 * ((b - r) / c + 2);
	// } else if (max == b) {
	// h = 60.0 * ((r - g) / c + 4);
	// }
	// s = i == 0 ? 0 : 1.0 - min / i;
	// }
	//
	// return new HsiSample(h, s, i, count);
	// }

}
