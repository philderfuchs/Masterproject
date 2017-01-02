package de.automatic.ui.colorconfiguraiton.services;

import java.util.Collections;

import de.automatic.ui.colorconfiguraiton.entities.Sample;
import de.automatic.ui.colorconfiguraiton.entities.SampleList;

public class SeedFilterer {

	private static final double threshold = 0.25;

	public static void filterSeeds(SampleList seeds) {
		Collections.shuffle(seeds);
		boolean removedSeed = true;
		while (removedSeed) {
			removedSeed = false;
			outer: for (int i = 0; i < seeds.size(); i++) {
				Sample s1 = seeds.get(i);
				for (int j = i + 1; j < seeds.size(); j++) {
					Sample s2 = seeds.get(j);
					if (ErrorCalculationService.getEucledianDistance(s1, s2) < threshold) {
						System.out.println("removed seed " + s2);
						seeds.remove(s2);
						removedSeed = true;
						break outer;
					}

				}

			}
		}

	}

}
