package de.automatic.ui.colorconfiguration.services;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

import de.automatic.ui.colorconfiguraiton.entities.HsiSample;
import de.automatic.ui.colorconfiguraiton.services.ColorSpaceConversionService;

public class ColorSpaceConversionServiceTest {

	// @Test
	// public void test0() {
	// System.out.println(ColorSpaceConversionService.toRgb(120.0, 1.0, 1.0 /
	// 3.0, 0));
	// }
	//
	// @Test
	// public void test1() {
	// System.out.println(ColorSpaceConversionService.getX(new HsiSample(0.0,
	// 0.0, 1.0 / 3.0, 0)));
	// }
	//
	// @Test
	// public void test2() {
	// System.out.println(ColorSpaceConversionService.getY(new HsiSample(0.0,
	// 0.0, 1.0 / 3.0, 0)));
	// }

	@Test
	public void tes3() {

		// System.out.println(ColorSpaceConversionService.toHsi(10, 0, 0, 0));
		System.out.println(ColorSpaceConversionService.toHsi(10, 10, 0, 0));
//		System.out.println(ColorSpaceConversionService.getX(ColorSpaceConversionService.toHsi(20, 0, 0, 0)));


		// // white
		// assertEquals(new HsiSample(0.0, 0.0, 1.0, 0),
		// ColorSpaceConversionService.toHsi(255, 255, 255, 0));
		//
		// // grey
		// assertEquals(new HsiSample(0.0, 0.0, 0.5, 0),
		// ColorSpaceConversionService.toHsi(0.5 * 255.0, 0.5 * 255.0, 0.5 *
		// 255.0, 0));
		//
		// // black
		// assertEquals(new HsiSample(0.0, 0.0, 0.0, 0),
		// ColorSpaceConversionService.toHsi(0.0 * 255.0, 0.0 * 255.0, 0.0 *
		// 255.0, 0));
		//
		// // red
		// assertEquals(new HsiSample(0.0, 1.0, 1.0 / 3.0, 0),
		// ColorSpaceConversionService.toHsi(1.0 * 255.0, 0.0 * 255.0, 0.0 *
		// 255.0, 0));
		//
		// // green
		// assertEquals(new HsiSample(120.0, 1.0, 1.0 / 3.0, 0),
		// ColorSpaceConversionService.toHsi(0.0 * 255.0, 1.0 * 255.0, 0.0 *
		// 255.0, 0));
		//
		// // cyan
		// assertEquals(new HsiSample(180.0, 1.0, 2.0 / 3.0, 0),
		// ColorSpaceConversionService.toHsi(0.0 * 255.0, 1.0 * 255.0, 1.0 *
		// 255.0, 0));
		//
		// // yellow
		// assertEquals(new HsiSample(60.0, 1.0, 2.0 / 3.0, 0),
		// ColorSpaceConversionService.toHsi(1.0 * 255.0, 1.0 * 255.0, 0.0 *
		// 255.0, 0));
		//
		// // blue
		// assertEquals(new HsiSample(240.0, 1.0, 1.0 / 3.0, 0),
		// ColorSpaceConversionService.toHsi(0.0 * 255.0, 0.0 * 255.0, 1.0 *
		// 255.0, 0));
		//
		// // #BFBF00
		// assertEquals(new HsiSample(60.0, 1.0, 0.5, 0),
		// ColorSpaceConversionService.toHsi(0.75 * 255.0, 0.75 * 255.0, 0.0 *
		// 255.0, 0));
		//
		// // #008000
		// assertEquals(new HsiSample(120.0, 1.0, 0.167, 0),
		// ColorSpaceConversionService.toHsi(0.0 * 255.0, 0.5 * 255.0, 0.0 *
		// 255.0, 0));
		//
		// // #008000
		// assertEquals(new HsiSample(180.0, 0.4, 0.833, 0),
		// ColorSpaceConversionService.toHsi(0.5 * 255.0, 1.0 * 255.0, 1.0 *
		// 255.0, 0));

	}

}
