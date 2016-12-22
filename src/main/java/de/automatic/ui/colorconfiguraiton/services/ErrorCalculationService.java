package de.automatic.ui.colorconfiguraiton.services;

import de.automatic.ui.colorconfiguraiton.entities.Sample;

public class ErrorCalculationService {
	
	public static double getSquaredDistance(Sample p1, Sample p2) {
		return Math.sqrt(Math.pow(p1.getC1() - p2.getC1(), 2) + Math.pow(p1.getC2() - p2.getC2(), 2)
				+ Math.pow(p1.getC3() - p2.getC3(), 2));
	}
	
	public static double getEucledianDistance(Sample p1, Sample p2) {
		return (Math.sqrt(Math.pow(p1.getC1() - p2.getC1(), 2) + Math.pow(p1.getC2() - p2.getC2(), 2)
				+ Math.pow(p1.getC3() - p2.getC3(), 2)));
	}
}
