package de.automatic.ui.colorconfiguraiton.vis;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.automatic.ui.colorconfiguraiton.entities.RgbSample;
import de.automatic.ui.colorconfiguraiton.entities.Sample;
import de.automatic.ui.colorconfiguraiton.services.ConversionService;

public class PaletteShower {

	private HashSet<Sample> palette;
	private int windowLength = 400;
	private int windowHeight = 200;
	private int x;
	private int y;
	String title;

	public PaletteShower(HashSet<Sample> palette, String title, int x, int y) {
		this.palette = palette;
		this.title = title;
		this.x = x;
		this.y = y;
	}

	public void visualizePalette() {

		JFrame jFrame = new JFrame();
		MyPanel panel = new MyPanel();
		jFrame.add(panel);

		jFrame.setSize(windowLength, windowHeight);
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

			int lengthOfQuader = windowLength / palette.size();
			int x = 0;
			for (Sample sample : palette) {
				RgbSample rgbSample = ConversionService.toRgb(sample);
				Color c = new Color((int) rgbSample.getC1(), (int) rgbSample.getC2(), (int) rgbSample.getC3());
				g.setColor(c);
				g.fillRect(x * lengthOfQuader, 0, lengthOfQuader, windowHeight);
				x++;
			}
		}

	}

}