package de.automatic.ui.colorconfiguraiton.process;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

import de.automatic.ui.colorconfiguraiton.entities.*;
import de.automatic.ui.colorconfiguraiton.services.ConversionService;

public class ImageReader {

	private BufferedImage img;

	public ImageReader(File file) throws IOException {
		img = ImageIO.read(file);
	}

	public SampleList getRgbHistogram() {
		HashMap<Integer, Integer> pixelMap = getPixelMap();

		SampleList samples = new SampleList();
		for (Integer i : pixelMap.keySet()) {
			Color c = new Color(i);
			samples.add(new RgbSample(c.getRed(), c.getGreen(), c.getBlue(), pixelMap.get(i)));
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

		HsiSample[][] converted = new HsiSample [imgHeight][imgWidth];
		for (int y = 0; y < imgHeight; y++) {
			for (int x = 0; x < imgWidth; x++) {
				Color c = new Color(img.getRGB(x, y));
				converted[x][y] = ConversionService.toHsi(c.getRed(), c.getGreen(), c.getBlue(), 1);
			}
		}
		
		for (int y = 0; y < imgHeight; y++) {
			for (int x = 0; x < imgWidth; x++) {
				HsiSample sample = converted[x][y];
				double weight = 1.0 + 2.0 * sample.getC2();
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
