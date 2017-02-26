package de.automatic.ui.colorconfiguration.segmentation;

import de.automatic.ui.colorconfiguraiton.entities.Histogram;

public class MaxDistanceTest implements StatisticalTest {

	private static final double maxDistance = 0.065;

	@Override
	public boolean similiar(Histogram histo1, Histogram histo2, int start, int end) {

		for (int i = start; i < end; i++) {
			if (Math.abs(histo1.get(i).getValue() - histo2.get(i).getValue()) > maxDistance) {
				return false;
			}
		}
		return true;
	}

}
