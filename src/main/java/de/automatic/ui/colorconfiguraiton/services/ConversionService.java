package de.automatic.ui.colorconfiguraiton.services;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import de.automatic.ui.colorconfiguraiton.entities.CartesianCoordinates;
import de.automatic.ui.colorconfiguraiton.entities.Channels;
import de.automatic.ui.colorconfiguraiton.entities.Cluster;
import de.automatic.ui.colorconfiguraiton.entities.ClusterContainer;
import de.automatic.ui.colorconfiguraiton.entities.Histogram;
import de.automatic.ui.colorconfiguraiton.entities.HistogramElement;
import de.automatic.ui.colorconfiguraiton.entities.HsiSample;
import de.automatic.ui.colorconfiguraiton.entities.RgbSample;
import de.automatic.ui.colorconfiguraiton.entities.Sample;
import de.automatic.ui.colorconfiguraiton.entities.SampleList;

public class ConversionService {

	public static SampleList toRgbSampleList(SampleList samples) {
		
		if(samples.get(0) instanceof RgbSample) {
			return samples;
		} else if (samples.get(0) instanceof HsiSample) {
			SampleList rgbSamples = new SampleList();
			for(Sample s : samples) {
				rgbSamples.add(toRgb(s));
			}
			return rgbSamples;
		}
		return samples;
	}
	
	public static Histogram toHistogram(SampleList samples, Channels channel, int bins, boolean normalize) {
	
		Histogram histo = new Histogram(bins, channel);
		samples.sort(channel);
		double binRange = 1.0 / bins;
		int j = 0;

		for (int i = 0; i < bins; i++) {
			int count = 0;
			SampleList binSamples = new SampleList();
			while (j < samples.size() && samples.get(j).getNormalized(channel) < (double) (i + 1.0) * binRange) {
				count += samples.get(j).getCount();
				binSamples.add(samples.get(j));
				j++;
			}
			histo.add(count);
			histo.get(histo.size() - 1).setSamples(binSamples);
		}

		if(normalize) {
			histo.normalize();
		}
		
		return histo;
	}

	public static HashSet<Sample> toHashSet(ClusterContainer clusters) {
		HashSet<Sample> pixelSet = new HashSet<>();
		for (Cluster c : clusters) {
			pixelSet.add(c.getCenter());
		}
		return pixelSet;
	}
	
	public static HashSet<Sample> toHashSet(SampleList samples) {
		HashSet<Sample> pixelSet = new HashSet<>();
		for (Sample s : samples) {
			pixelSet.add(s);
		}
		return pixelSet;
	}

	public static RgbSample toRgb(CartesianCoordinates coord, int count) {
		return new RgbSample(coord.getX() * 255, coord.getY() * 255, coord.getZ() * 255, count);
	}

	public static HsiSample toHsi(CartesianCoordinates coord, int count) {
		double h = Math.toDegrees(Math.atan(coord.getY() / coord.getX()));
		if (coord.getX() < 0) {
			h += 180.0;
		} else if (coord.getX() >= 0 && coord.getY() < 0) {
			h += 360.0;
		}
		double s = Math.sqrt(Math.pow(coord.getX(), 2) + Math.pow(coord.getY(), 2));
		double i = coord.getZ();
		return new HsiSample(h, s, i, count);
	}

	public static CartesianCoordinates toCoordinates(Sample s) {
		if (s instanceof RgbSample) {
			return new CartesianCoordinates(s.getC1Normalized(), s.getC2Normalized(), s.getC3Normalized());
		} else if (s instanceof HsiSample) {
			if (s.getC1() == 0) {
				return new CartesianCoordinates(s.getC2(), 0.0, s.getC3Normalized());
			} else if (s.getC1() == 180) {
				return new CartesianCoordinates(-1.0 * s.getC2(), 0.0, s.getC3Normalized());
			} else if (s.getC1() == 90.0) {
				return new CartesianCoordinates(0.0, s.getC2(), s.getC3Normalized());
			} else if (s.getC1() == 270.0) {
				return new CartesianCoordinates(0.0, -1.0 * s.getC2(), s.getC3Normalized());
			} else {
				return new CartesianCoordinates(Math.cos(Math.toRadians(s.getC1())) * s.getC2(),
						Math.sin(Math.toRadians(s.getC1())) * s.getC2(), s.getC3Normalized());
			}
		}
		return new CartesianCoordinates(0.0, 0.0, 0.0);

	}

	public static RgbSample toRgb(Sample p) {
		if (p instanceof RgbSample) {
			return new RgbSample((RgbSample) p);
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
		if (c != 0.0) {
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
