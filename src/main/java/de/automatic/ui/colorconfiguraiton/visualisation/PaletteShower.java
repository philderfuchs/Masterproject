package de.automatic.ui.colorconfiguraiton.visualisation;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.automatic.ui.colorconfiguraiton.entities.RgbSample;
import de.automatic.ui.colorconfiguraiton.entities.Sample;
import de.automatic.ui.colorconfiguraiton.entities.SampleList;
import de.automatic.ui.colorconfiguraiton.services.ConversionService;

public class PaletteShower {

	private SampleList samples;
	private int colorWidth = 30;
	private int windowHeight = 100;
	private int x;
	private int y;
	String title;

	public PaletteShower(SampleList samples, String title, int x, int y) {
		this.samples = samples;
		this.title = title;
		this.x = x;
		this.y = y;
		this.visualizePalette();
	}

	public void visualizePalette() {

		JFrame jFrame = new JFrame();
		MyPanel panel = new MyPanel();
		jFrame.add(panel);

		jFrame.setSize(colorWidth * samples.size(), windowHeight);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setTitle(title);
		jFrame.setLocation(x, y);
		jFrame.setVisible(true);

	}

	class MyPanel extends JPanel {

		MyPanel() {
			this.setOpaque(true);
		}

		public void paintComponent(Graphics g) {

			int x = 0;
			for (Sample sample : samples) {
				RgbSample rgbSample = ConversionService.toRgb(sample);
				Color c = new Color((int) rgbSample.getC1(), (int) rgbSample.getC2(), (int) rgbSample.getC3());
				g.setColor(c);
				g.fillRect(x++ * colorWidth, 0, colorWidth, windowHeight);
			}
		}

	}

}