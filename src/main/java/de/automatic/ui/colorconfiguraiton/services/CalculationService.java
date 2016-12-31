package de.automatic.ui.colorconfiguraiton.services;

import de.automatic.ui.colorconfiguraiton.entities.CartesianCoordinates;
import de.automatic.ui.colorconfiguraiton.entities.HsiSample;
import de.automatic.ui.colorconfiguraiton.entities.RgbSample;
import de.automatic.ui.colorconfiguraiton.entities.Sample;
import de.automatic.ui.colorconfiguraiton.entities.SampleList;

public class CalculationService {

	public static Sample calculateMean(SampleList samples) {
		double meanX = 0;
		double meanY = 0;
		double meanZ = 0;
		for (Sample s : samples) {
			CartesianCoordinates coord = ConversionService.toCoordinates(s);
			meanX += coord.getX() * (double) s.getCount();
			meanY += coord.getY() * (double) s.getCount();
			meanZ += coord.getZ() * (double) s.getCount();
		}
		meanX = meanX / (double) samples.getCountOfPixels();
		meanY = meanY / (double) samples.getCountOfPixels();
		meanZ = meanZ / (double) samples.getCountOfPixels();
		Sample mean = null;
		if (samples.get(0) instanceof RgbSample) {
			mean = ConversionService.toRgb(new CartesianCoordinates(meanX, meanY, meanZ), samples.getCountOfPixels());
		} else if (samples.get(0) instanceof HsiSample) {
			mean = ConversionService.toHsi(new CartesianCoordinates(meanX, meanY, meanZ), samples.getCountOfPixels());
		}
		return mean;
	}

}
