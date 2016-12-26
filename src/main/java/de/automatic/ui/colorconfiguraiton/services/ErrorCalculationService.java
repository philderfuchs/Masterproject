package de.automatic.ui.colorconfiguraiton.services;

import de.automatic.ui.colorconfiguraiton.entities.CartesianCoordinates;
import de.automatic.ui.colorconfiguraiton.entities.Sample;

public class ErrorCalculationService {

	public static double getSquaredDistance(Sample p1, Sample p2) {
		CartesianCoordinates coordP1 = ConversionService.toCoordinates(p1);
		CartesianCoordinates coordP2 = ConversionService.toCoordinates(p2);

		double dx = coordP1.getX() - coordP2.getX();
		double dy = coordP1.getY() - coordP2.getY();
		double dz = coordP1.getZ() - coordP2.getZ();

		return Math.pow(dx, 2) + Math.pow(dy, 2) + Math.pow(dz, 2);
	}

	public static double getEucledianDistance(Sample p1, Sample p2) {
		CartesianCoordinates coordP1 = ConversionService.toCoordinates(p1);
		CartesianCoordinates coordP2 = ConversionService.toCoordinates(p2);
		
		double dx = coordP1.getX() - coordP2.getX();
		double dy = coordP1.getY() - coordP2.getY();
		double dz = coordP1.getZ() - coordP2.getZ();

		return (Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2) + Math.pow(dz, 2)));
	}
}
