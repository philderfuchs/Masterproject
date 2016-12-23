package de.automatic.ui.colorconfiguraiton.vis;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.automatic.ui.colorconfiguraiton.entities.RgbSample;
import de.automatic.ui.colorconfiguraiton.entities.Sample;
import de.automatic.ui.colorconfiguraiton.services.ColorSpaceConversionService;

public class PaletteShower {

	HashSet<Sample> palette;
	int windowLength = 400;
	int windowHeight = 200;
	String title;

	public PaletteShower(HashSet<Sample> palette, String title) {
		this.palette = palette;
		this.title = title;
	}

	public void visualizePalette() {

		JFrame jFrame = new JFrame();
		MyPanel panel = new MyPanel();
		jFrame.add(panel);

		jFrame.setSize(windowLength, windowHeight);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setTitle(title);
		jFrame.setVisible(true);

	}

	class MyPanel extends JPanel {

		MyPanel() {
			this.setOpaque(true);
		}

		public void paintComponent(Graphics g) {

			int lengthOfQuader = windowLength / palette.size();
			int x = 0;
			for (Sample sample : palette) {
				RgbSample rgbSample = ColorSpaceConversionService.toRgb(sample);
				Color c = new Color((int) rgbSample.getC1(), (int) rgbSample.getC2(), (int) rgbSample.getC3());
				g.setColor(c);
				g.fillRect(x * lengthOfQuader, 0, lengthOfQuader, windowHeight);
				x++;
			}
		}

	}

}