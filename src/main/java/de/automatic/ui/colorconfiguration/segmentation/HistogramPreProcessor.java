package de.automatic.ui.colorconfiguration.segmentation;

import de.automatic.ui.colorconfiguraiton.entities.Histogram;
import de.automatic.ui.colorconfiguraiton.entities.HistogramElement;

public class HistogramPreProcessor {

	private static final double threshold = 3;

	/**
	 * compresses the histogram if integral below threshold
	 * 
	 * @param histo
	 *            the histogram to compress
	 * @return if compresses got applied
	 */
	public static boolean compress(Histogram histo) {

		double integral = 0;

		for (HistogramElement e : histo) {
			integral += e.getValue();
		}
		if (integral < threshold) {
			for (HistogramElement e : histo) {
				e.setValue(Math.sqrt(Math.sqrt(e.getValue())));
			}
			return true;
		}
		return false;
	}

	public static boolean smooth(Histogram histo) {
		double oldValue = histo.get(0).getValue();
		for (int i = 1; i < histo.size() - 1; i++) {
			double value = (oldValue + 2 * histo.get(i).getValue() + histo.get(i+1).getValue()) / 4;
			oldValue = histo.get(i).getValue();
			histo.get(i).setValue(value);
		}
		return true;

	}

}
