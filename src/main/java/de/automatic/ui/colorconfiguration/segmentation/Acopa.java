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

	private static final int histoBins = 64;

	private FtcSegmentation segmentor;

	public Acopa() {

	}

	public HierarchicalHsiPalette findSeeds(SampleList samples) {
		segmentor = new FtcSegmentation();

		HierarchicalHsiPalette hieraPalette = new HierarchicalHsiPalette();

		Histogram histo = ConversionService.toHistogram(samples, Channels.C1, histoBins, true);
		Segmentation seg = segmentor.segment(histo, "C1");

		for (int i = 0; i < seg.size() - 2; i++) {
			SampleList modeSamples = getSamplesForMode(histo, seg, i);
			HierarchicalHsiSample child = new HierarchicalHsiSample(CalculationService.calculateMean(modeSamples),
					modeSamples);
			hieraPalette.add(child);
			rek(child, 1);
		}

		return hieraPalette;
	}

	private void rek(HierarchicalHsiSample s, int level) {
		if (level == 3)
			return;
		Channels c = getChannel(level);
		Histogram modeHisto = ConversionService.toHistogram(s.getModeSamples(), c, histoBins, true);
		Segmentation modeSeg = segmentor.segment(modeHisto, "Mode level " + level);
		if (modeSeg.size() == 2) {
			SampleList modeSamples = s.getModeSamples();
			HierarchicalHsiSample child = new HierarchicalHsiSample(CalculationService.calculateMean(modeSamples),
					modeSamples);
			s.getChildren().add(child);
			rek(child, level + 1);
		} else {
			for (int i = 0; i < modeSeg.size() - 2; i++) {
				SampleList modeSamples = getSamplesForMode(modeHisto, modeSeg, i);
				HierarchicalHsiSample child = new HierarchicalHsiSample(CalculationService.calculateMean(modeSamples),
						modeSamples);
				s.getChildren().add(child);
				rek(child, level + 1);
			}
		}
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

	private SampleList getSamplesForMode(Histogram histo, Segmentation seg, int modeMarker) {
		SampleList modeSamples = new SampleList();

		if (modeMarker == 0) {
			for (int j = seg.get(0); j < seg.get(1); j++) {
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
