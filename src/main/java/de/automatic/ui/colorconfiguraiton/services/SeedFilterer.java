package de.automatic.ui.colorconfiguraiton.services;

import java.util.Collections;

import de.automatic.ui.colorconfiguraiton.entities.Sample;
import de.automatic.ui.colorconfiguraiton.entities.SampleList;

public class SeedFilterer {

	private static final double threshold = 0.2;

	public static void filterSeeds(SampleList seeds) {
		Collections.shuffle(seeds);
		boolean removedSeed = true;
		while (removedSeed) {
			removedSeed = false;
			outer: for (int i = 0; i < seeds.size(); i++) {
				Sample s1 = seeds.get(i);
				for (int j = i + 1; j < seeds.size(); j++) {
					Sample s2 = seeds.get(j);
					if (ErrorCalculationService.getEucledianDistance(ConversionService.toHsi(s1),
							ConversionService.toHsi(s2)) < threshold) {
						System.out.println("removed seed " + s2);
						if (accDistance(s1, seeds) < accDistance(s2, seeds)) {
							seeds.remove(s1);
						} else {
							seeds.remove(s2);
						}

						removedSeed = true;
						break outer;
					}

				}

			}
		}

	}

	private static double accDistance(Sample s, SampleList samples) {
		double distance = 0;
		for (Sample s2 : samples) {
			distance += ErrorCalculationService.getEucledianDistance(s, s2);
		}
		return distance;
	}

}
