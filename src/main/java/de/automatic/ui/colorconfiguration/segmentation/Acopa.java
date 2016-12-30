package de.automatic.ui.colorconfiguration.segmentation;

import de.automatic.ui.colorconfiguraiton.entities.Channels;
import de.automatic.ui.colorconfiguraiton.entities.Histogram;
import de.automatic.ui.colorconfiguraiton.entities.Sample;
import de.automatic.ui.colorconfiguraiton.entities.SampleList;
import de.automatic.ui.colorconfiguraiton.entities.Segmentation;
import de.automatic.ui.colorconfiguraiton.services.CalculationService;
import de.automatic.ui.colorconfiguraiton.services.ConversionService;

public class Acopa {

	public SampleList findSeeds(SampleList samples) {
		Histogram histo = ConversionService.toHistogram(samples, Channels.C2, 64, true);

		FtcSegmentation segmentor = new FtcSegmentation();
		Segmentation seg = segmentor.segment(histo);
		
		SampleList seeds = new SampleList();
		for (int i = 0; i < seg.size() - 2; i++) {
			if (i == 0) {
				SampleList modeSamples = new SampleList();
				for (int j = seg.get(i); j < seg.get(i + 1); j++) {
					for (Sample s : histo.get(j).getSamples()) {
						modeSamples.add(s);
					}
				}
				for (int j = seg.get(seg.size() - 2); j < seg.get(seg.size() - 1); j++) {
					for (Sample s : histo.get(j).getSamples()) {
						modeSamples.add(s);
					}
				}
				seeds.add(CalculationService.calculateMean(modeSamples));

			} else {
				SampleList modeSamples = new SampleList();
				for (int j = seg.get(i); j < seg.get(i + 1); j++) {
					for (Sample s : histo.get(j).getSamples()) {
						modeSamples.add(s);
					}
				}
				seeds.add(CalculationService.calculateMean(modeSamples));
			}
		}
		return seeds;
	}
	
}
