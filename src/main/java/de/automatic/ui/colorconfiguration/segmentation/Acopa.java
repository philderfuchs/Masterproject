package de.automatic.ui.colorconfiguration.segmentation;

import de.automatic.ui.colorconfiguraiton.entities.Channels;
import de.automatic.ui.colorconfiguraiton.entities.HierarchicalHsiPalette;
import de.automatic.ui.colorconfiguraiton.entities.Histogram;
import de.automatic.ui.colorconfiguraiton.entities.Sample;
import de.automatic.ui.colorconfiguraiton.entities.SampleList;
import de.automatic.ui.colorconfiguraiton.entities.Segmentation;
import de.automatic.ui.colorconfiguraiton.services.CalculationService;
import de.automatic.ui.colorconfiguraiton.services.ConversionService;

public class Acopa {

	public SampleList findSeeds(SampleList samples) {
		SampleList hieraPalette = new SampleList();

		FtcSegmentation segmentor = new FtcSegmentation();
		Histogram histo = ConversionService.toHistogram(samples, Channels.C1, 64, true);
		Segmentation seg = segmentor.segment(histo);

		for (int i = 0; i < seg.size() - 2; i++) {
			SampleList modeSamples = getSamplesForMode(histo, seg, i);
			hieraPalette.add(new HierarchicalHsiPalette(CalculationService.calculateMean(modeSamples), modeSamples));
		}
		for (Sample s : hieraPalette) {
			
		}
		return hieraPalette;
	}

	private SampleList getSamplesForMode(Histogram histo, Segmentation seg, int modeMarker) {
		SampleList modeSamples = new SampleList();

		if (modeMarker == 0) {
			for (int j = seg.get(modeMarker); j < seg.get(modeMarker + 1); j++) {
				for (Sample s : histo.get(j).getSamples()) {
					modeSamples.add(s);
				}
			}
			for (int j = seg.get(seg.size() - 2); j < seg.get(seg.size() - 1); j++) {
				for (Sample s : histo.get(j).getSamples()) {
					modeSamples.add(s);
				}
			}

		} else {
			for (int j = seg.get(modeMarker); j < seg.get(modeMarker + 1); j++) {
				for (Sample s : histo.get(j).getSamples()) {
					modeSamples.add(s);
				}
			}
		}
		return modeSamples;
	}

}
