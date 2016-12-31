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

	public HierarchicalHsiPalette findSeeds(SampleList samples) {
		FtcSegmentation segmentor = new FtcSegmentation();

		HierarchicalHsiPalette hieraPalette = segmentHChannel(samples, segmentor);

		int mode = 0;
		for (Sample s : hieraPalette) {
			HierarchicalHsiSample modePalette = (HierarchicalHsiSample) s;
			Histogram modeHisto = ConversionService.toHistogram(modePalette.getModeSamples(), Channels.C2, 64, true);
			Segmentation modeSeg = segmentor.segment(modeHisto, "Mode " + mode++);

			for (int i = 0; i < modeSeg.size() - 2; i++) {
				SampleList modeSamples = getSamplesForMode(modeHisto, modeSeg, i);
				modePalette.getChildren()
						.add(new HierarchicalHsiSample(CalculationService.calculateMean(modeSamples), modeSamples));
			}
		}
		return hieraPalette;
	}

	private HierarchicalHsiPalette segmentHChannel(SampleList samples, FtcSegmentation segmentor) {
		HierarchicalHsiPalette hieraPalette = new HierarchicalHsiPalette();

		Histogram histo = ConversionService.toHistogram(samples, Channels.C1, 64, true);
		Segmentation seg = segmentor.segment(histo, "C1");

		for (int i = 0; i < seg.size() - 2; i++) {
			SampleList modeSamples = getSamplesForMode(histo, seg, i);
			hieraPalette.add(new HierarchicalHsiSample(CalculationService.calculateMean(modeSamples), modeSamples));
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
