package de.automatic.ui.colorconfiguration.segmentation;

import de.automatic.ui.colorconfiguraiton.entities.Histogram;
import de.automatic.ui.colorconfiguraiton.entities.HistogramElement;

public class HistogramCompressor {

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

}
