package de.automatic.ui.colorconfiguraiton.visualisation;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.automatic.ui.colorconfiguraiton.entities.HierarchicalHsiPalette;
import de.automatic.ui.colorconfiguraiton.entities.HierarchicalHsiSample;
import de.automatic.ui.colorconfiguraiton.entities.RgbSample;
import de.automatic.ui.colorconfiguraiton.entities.Sample;
import de.automatic.ui.colorconfiguraiton.entities.SampleList;
import de.automatic.ui.colorconfiguraiton.services.ConversionService;

public class HierarchicalPaletteShower {

	private final int colorWidth = 50;
	private final int colorHeight = 80;

	private HierarchicalHsiPalette hieraPalette;
	private int x;
	private int y;
	String title;
	private JFrame jFrame;

	public HierarchicalPaletteShower(HierarchicalHsiPalette hieraPalette, String title, int x, int y) {
		this.hieraPalette = hieraPalette;
		this.title = title;
		this.x = x;
		this.y = y;
		this.visualizePalette();
	}

	public void visualizePalette() {

		jFrame = new JFrame();
		MyPanel panel = new MyPanel();
		jFrame.add(panel);

		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setTitle(title);
		jFrame.setLocation(x, y);
		jFrame.setSize(1500, 2 * colorHeight);
		jFrame.setVisible(true);

	}

	class MyPanel extends JPanel {

		MyPanel() {
			this.setOpaque(true);
		}

		public void paintComponent(Graphics g) {
			hieraPaint(hieraPalette, g, 0);
		}

		int level2Offset = 0;
		int level1Offset = 0;
		int level0Offset = 0;

		private int hieraPaint(HierarchicalHsiPalette palette, Graphics g, int level) {
			int returnCount = 0;

			for (HierarchicalHsiSample s : palette) {
				if (level == 1) {
					paintColor(g, colorWidth, level1Offset * colorWidth, 1 * colorHeight, s);
					level1Offset++;
					returnCount++;
				} else if (level == 0) {
					int countOfChildren = hieraPaint(s.getChildren(), g, level + 1);
					paintColor(g, countOfChildren * colorWidth, level0Offset * colorWidth, 0, s);
					level0Offset += countOfChildren;
				}
			}

			return returnCount;
		}

		private void paintColor(Graphics g, int width, int x, int y, Sample sample) {
			RgbSample rgbSample = ConversionService.toRgb(sample);
			Color c = new Color((int) rgbSample.getC1(), (int) rgbSample.getC2(), (int) rgbSample.getC3());
			g.setColor(c);
			g.fillRect(x, y, width, colorHeight);
		}

	}

}
