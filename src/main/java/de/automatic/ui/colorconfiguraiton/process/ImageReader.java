package de.automatic.ui.colorconfiguraiton.process;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import de.automatic.ui.colorconfiguraiton.entities.*;
import de.automatic.ui.colorconfiguraiton.services.CalculationService;
import de.automatic.ui.colorconfiguraiton.services.ConversionService;
import de.automatic.ui.colorconfiguraiton.services.ErrorCalculationService;

public class ImageReader {

	private BufferedImage img;

	public ImageReader(File file) throws IOException {
		img = ImageIO.read(file);
	}

	public SampleList getRgbHistogram() {
		HashMap<HsiSample, Double> pixelMap = getWeightedHsiMap();

		SampleList samples = new SampleList();
		for (HsiSample s : pixelMap.keySet()) {
			s.setCount(pixelMap.get(s).intValue());
			samples.add(ConversionService.toRgb(s));
		}

		return samples;
	}

	public SampleList getHsiHistogram() {
		HashMap<HsiSample, Double> pixelMap = getWeightedHsiMap();

		SampleList samples = new SampleList();
		for (HsiSample s : pixelMap.keySet()) {
			s.setCount(pixelMap.get(s).intValue());
			samples.add(s);
		}

		return samples;
	}

	private HashMap<HsiSample, Double> getWeightedHsiMap() {
		int imgHeight = img.getHeight();
		int imgWidth = img.getWidth();

		HashMap<HsiSample, Double> pixelMap = new HashMap();

		HsiSample[][] converted = new HsiSample[imgWidth][imgHeight];
		for (int y = 0; y < imgHeight; y++) {
			for (int x = 0; x < imgWidth; x++) {
				Color c = new Color(img.getRGB(x, y));
				converted[x][y] = ConversionService.toHsi(c.getRed(), c.getGreen(), c.getBlue(), 1);
			}
		}

		for (int y = 1; y < imgHeight - 1; y++) {
			for (int x = 1; x < imgWidth - 1; x++) {
				HsiSample sample = converted[x][y];
				// double weight = 1.0 + 2.0 * sample.getC2();

				double meanDist = 0.0;
				for (int i = -1; i <= 1; i++) {
					for (int j = -1; j <= 1; j++) {
						meanDist += ErrorCalculationService.getEucledianDistance(sample, converted[x + i][y + j]);
					}
				}
				meanDist /= 8;
				meanDist += 0.01;
				meanDist = 1.0 / (meanDist * 100);
				double weight = 1.0 + Math.pow(6.0 * sample.getC2() * meanDist, 2);

				if (pixelMap.containsKey(sample)) {
					pixelMap.put(sample, pixelMap.get(sample) + weight);
				} else {
					pixelMap.put(sample, weight);
				}
			}
		}
		return pixelMap;
	}

	private HashMap<Integer, Integer> getPixelMap() {
		int imgHeight = img.getHeight();
		int imgWidth = img.getWidth();

		HashMap<Integer, Integer> pixelMap = new HashMap();
		Integer currentPixelColor;

		for (int y = 0; y < imgHeight; y++) {
			for (int x = 0; x < imgWidth; x++) {
				currentPixelColor = img.getRGB(x, y);
				if (pixelMap.containsKey(currentPixelColor)) {
					pixelMap.put(currentPixelColor, pixelMap.get(currentPixelColor) + 1);
				} else {
					pixelMap.put(currentPixelColor, 1);
				}
			}
		}
		return pixelMap;
	}

}
