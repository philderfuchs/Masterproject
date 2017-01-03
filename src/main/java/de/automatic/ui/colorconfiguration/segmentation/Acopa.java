package de.automatic.ui.colorconfiguration.segmentation;

import de.automatic.ui.colorconfiguraiton.entities.Channels;
import de.automatic.ui.colorconfiguraiton.entities.HierarchicalHsiPalette;
import de.automatic.ui.colorconfiguraiton.entities.HierarchicalHsiSample;
import de.automatic.ui.colorconfiguraiton.entities.Histogram;
import de.automatic.ui.colorconfiguraiton.entities.Sample;
import de.automatic.ui.colorconfiguraiton.entities.SampleList;
import de.automatic.ui.colorconfiguraiton.entities.Segmentation;
import de.automatic.ui.colorconfiguraiton.services.CalculationService;
import de.automatic.ui.colorconfiguraiton.services.ConversionService;

public class Acopa {

	private static final int histoBins = 128;
	private static final boolean weightedMean = true;

	private FtcSegmentation segmentor;
	private GreyCylinderFilterer filterer;

	public Acopa() {
		segmentor = new FtcSegmentation();
		filterer = new GreyCylinderFilterer();
	}

	public HierarchicalHsiPalette findSeeds(SampleList samples) {
		HierarchicalHsiPalette hieraPalette = new HierarchicalHsiPalette();
		SampleList filteredSamples = filterer.filterGreyCylinder(samples, 0.2);

		Histogram histo = ConversionService.toHistogram(filteredSamples, Channels.C1, histoBins, true);
		Segmentation seg = segmentor.segment(histo, "C1");
		filterer.linkBackGreyCylinder(histo);

		// The Last modemarker is always the end of the histogram and thus
		// doesnt have a successor
		for (int i = 0; i < seg.size() - 1; i++) {
			SampleList modeSamples = getSamplesForMode(histo, seg, i);
			HierarchicalHsiSample child = new HierarchicalHsiSample(
					CalculationService.calculateMean(modeSamples, weightedMean), modeSamples);
			hieraPalette.add(child);
			rek(child, 1);
		}

		return hieraPalette;
	}

	private void rek(HierarchicalHsiSample s, int level) {
		if (level == 3)
			return;
		Channels channel = getChannel(level);
		Histogram modeHisto = ConversionService.toHistogram(s.getModeSamples(), channel, histoBins, true);
		Segmentation modeSeg = segmentor.segment(modeHisto, "Mode level " + level);

		// if (modeSeg.size() == 2) {
		// SampleList modeSamples = s.getModeSamples();
		// HierarchicalHsiSample child = new
		// HierarchicalHsiSample(CalculationService.calculateMean(modeSamples,
		// true),
		// modeSamples);
		// s.getChildren().add(child);
		// rek(child, level + 1);
		// } else {

		// The Last modemarker is always the end of the histogram and thus
		// doesnt have a successor
		for (int i = 0; i < modeSeg.size() - 1; i++) {
			SampleList modeSamples = getSamplesForMode(modeHisto, modeSeg, i);
			HierarchicalHsiSample child = new HierarchicalHsiSample(
					CalculationService.calculateMean(modeSamples, weightedMean), modeSamples);
			s.getChildren().add(child);
			rek(child, level + 1);
		}
		// }
	}

	private Channels getChannel(int level) {
		switch (level) {
		case 0:
			return Channels.C1;
		case 1:
			return Channels.C2;
		case 2:
			return Channels.C3;
		default:
			return Channels.C1;
		}
	}

	private SampleList getSamplesForMode(Histogram histo, Segmentation seg, int segIndex) {
		SampleList modeSamples = new SampleList();

		for (int j = seg.get(segIndex); j < seg.get(segIndex + 1); j++) {
			for (Sample s : histo.get(j).getSamples()) {
				modeSamples.add(s);
			}
		}

		return modeSamples;
	}

}
