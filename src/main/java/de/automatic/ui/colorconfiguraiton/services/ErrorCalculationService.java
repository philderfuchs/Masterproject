package de.automatic.ui.colorconfiguraiton.services;

import de.automatic.ui.colorconfiguraiton.entities.Pixel;

public class ErrorCalculationService {
	
	public static double getSquaredDistance(Pixel p1, Pixel p2) {
		return Math.sqrt(Math.pow(p1.getR() - p2.getR(), 2) + Math.pow(p1.getG() - p2.getG(), 2)
				+ Math.pow(p1.getB() - p2.getB(), 2));
	}
	
	public static double getEucledianDistance(Pixel p1, Pixel p2) {
		return (Math.sqrt(Math.pow(p1.getR() - p2.getR(), 2) + Math.pow(p1.getG() - p2.getG(), 2)
				+ Math.pow(p1.getB() - p2.getB(), 2)));
	}
}
