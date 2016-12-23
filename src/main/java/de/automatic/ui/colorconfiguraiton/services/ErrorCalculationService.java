package de.automatic.ui.colorconfiguraiton.services;

import de.automatic.ui.colorconfiguraiton.entities.Sample;

public class ErrorCalculationService {

	public static double getSquaredDistance(Sample p1, Sample p2) {
		double dx = ColorSpaceConversionService.getX(p1) - ColorSpaceConversionService.getX(p2);
		double dy = ColorSpaceConversionService.getY(p1) - ColorSpaceConversionService.getY(p2);
		double dz = ColorSpaceConversionService.getZ(p1) - ColorSpaceConversionService.getZ(p2);

		return Math.pow(dx, 2) + Math.pow(dy, 2) + Math.pow(dz, 2);
	}

	public static double getEucledianDistance(Sample p1, Sample p2) {
		double dx = ColorSpaceConversionService.getX(p1) - ColorSpaceConversionService.getX(p2);
		double dy = ColorSpaceConversionService.getY(p1) - ColorSpaceConversionService.getY(p2);
		double dz = ColorSpaceConversionService.getZ(p1) - ColorSpaceConversionService.getZ(p2);

		return (Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2) + Math.pow(dz, 2)));
	}
}
