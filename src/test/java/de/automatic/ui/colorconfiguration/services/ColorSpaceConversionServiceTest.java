package de.automatic.ui.colorconfiguration.services;

import static org.junit.Assert.*;

import org.junit.Test;

import de.automatic.ui.colorconfiguraiton.entities.HsiSample;
import de.automatic.ui.colorconfiguraiton.services.ColorSpaceConversionService;

public class ColorSpaceConversionServiceTest {
	
	@Test
	public void test1() {
		// white
		assertEquals(new HsiSample(0.0, 0.0, 1.0, 0), ColorSpaceConversionService.toHsi(255, 255, 255, 0));
		
		// grey
		assertEquals(new HsiSample(0.0, 0.0, 0.5, 0), ColorSpaceConversionService.toHsi(0.5 * 255.0, 0.5 * 255.0, 0.5 * 255.0, 0));
		
		// black
		assertEquals(new HsiSample(0.0, 0.0, 0.0, 0), ColorSpaceConversionService.toHsi(0.0 * 255.0, 0.0 * 255.0, 0.0 * 255.0, 0));

		// red
		assertEquals(new HsiSample(0.0, 1.0, 1.0/3.0, 0), ColorSpaceConversionService.toHsi(1.0 * 255.0, 0.0 * 255.0, 0.0 * 255.0, 0));

		// #BFBF00
		assertEquals(new HsiSample(60.0, 1.0, 0.5, 0), ColorSpaceConversionService.toHsi(0.75 * 255.0, 0.75 * 255.0, 0.0 * 255.0, 0));
		
		// #008000
		assertEquals(new HsiSample(120.0, 1.0, 0.167, 0), ColorSpaceConversionService.toHsi(0.0 * 255.0, 0.5 * 255.0, 0.0 * 255.0, 0));

	}
	
}
