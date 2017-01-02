package de.automatic.ui.colorconfiguration.segmentation;

import de.automatic.ui.colorconfiguraiton.entities.Histogram;
import de.automatic.ui.colorconfiguraiton.entities.HistogramElement;

public class GrenanderEstimator {

	public static Histogram poolAdjacentViolator(String direction, Histogram histo, int start, int end) {
		Histogram histo2 = direction.equals("dec") ? decreasingGrenanderEstimator(histo, start, end)
				: increasingGrenanderEstimator(histo, start, end);

		for (int i = 0; i < 10; i++) {
			histo2 = direction.equals("dec") ? decreasingGrenanderEstimator(histo2, start, end)
					: increasingGrenanderEstimator(histo2, start, end);
		}
		return histo2;
	}

	private static Histogram decreasingGrenanderEstimator(Histogram histo, int start, int end) {
		Histogram gHisto = new Histogram(histo);

		for (int i = start; i < end - 1; i++) {
			if (i + 1 < gHisto.getBins() && gHisto.get(i + 1).getValue() - gHisto.get(i).getValue() > 0.0) {
				// look ahead
				int j = i;
				double mean = gHisto.get(i).getValue();
				do {
					j++;
					mean += gHisto.get(j).getValue();
				} while (j + 1 <= end && gHisto.get(j + 1).getValue() - gHisto.get(j).getValue() > 0.0);

				mean /= ((double) j - (double) i + 1.0);
				for (int k = i; k <= j; k++) {
					gHisto.set(k, new HistogramElement(histo.get(k).getKey(), mean));
				}
				i = j - 1;
			}

		}

		return gHisto;
	}

	private static Histogram increasingGrenanderEstimator(Histogram histo, int start, int end) {
		Histogram gHisto = new Histogram(histo);

		for (int i = start; i < end - 1; i++) {
			if (i + 1 < gHisto.getBins() && gHisto.get(i + 1).getValue() - gHisto.get(i).getValue() < 0.0) {
				int j = i;
				double mean = gHisto.get(i).getValue();
				do {
					j++;
					mean += gHisto.get(j).getValue();
				} while (j + 1 <= end && gHisto.get(j + 1).getValue() - gHisto.get(j).getValue() < 0.0);

				mean /= ((double) j - (double) i + 1);
				for (int k = i; k <= j; k++) {
					gHisto.set(k, new HistogramElement(histo.get(k).getKey(), mean));
				}
				i = j - 1;
			}

		}
		return gHisto;
	}

}
