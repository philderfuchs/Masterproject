package de.automatic.ui.colorconfiguraiton.services;

import de.automatic.ui.colorconfiguraiton.entities.CartesianCoordinates;
import de.automatic.ui.colorconfiguraiton.entities.HsiSample;
import de.automatic.ui.colorconfiguraiton.entities.RgbSample;
import de.automatic.ui.colorconfiguraiton.entities.Sample;
import de.automatic.ui.colorconfiguraiton.entities.SampleList;

public class CalculationService {

	public static Sample calculateMean(SampleList samples, boolean weighted) {
		double meanX = 0;
		double meanY = 0;
		double meanZ = 0;
		for (Sample s : samples) {
			CartesianCoordinates coord = ConversionService.toCoordinates(s);
			double x = weighted ? coord.getX() * (double) s.getCount() : coord.getX();
			double y = weighted ? coord.getY() * (double) s.getCount() : coord.getY();
			double z = weighted ? coord.getZ() * (double) s.getCount() : coord.getZ();

			meanX += x;
			meanY += y;
			meanZ += z;
		}
		meanX = weighted ? meanX / (double) samples.getCountOfPixels() : meanX / (double) samples.size();
		meanY = weighted ? meanY / (double) samples.getCountOfPixels() : meanY / (double) samples.size();
		meanZ = weighted ? meanZ / (double) samples.getCountOfPixels() : meanZ / (double) samples.size();
		Sample mean = null;
		if (samples.get(0) instanceof RgbSample) {
			mean = ConversionService.toRgb(new CartesianCoordinates(meanX, meanY, meanZ), samples.getCountOfPixels());
		} else if (samples.get(0) instanceof HsiSample) {
			mean = ConversionService.toHsi(new CartesianCoordinates(meanX, meanY, meanZ), samples.getCountOfPixels());
		}
		return mean;
	}

}
