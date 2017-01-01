package de.automatic.ui.colorconfiguraiton.visualisation;

import java.awt.Color;
import java.awt.Dimension;
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
		jFrame.setSize(hieraPalette.getCountOfLastLevelChildren() * colorWidth, 3 * colorHeight);
		// jFrame.pack();
		jFrame.setVisible(true);

	}

	class MyPanel extends JPanel {

		MyPanel() {
			this.setOpaque(true);
		}

		public void paintComponent(Graphics g) {
			// hieraPaint(hieraPalette, g, 0);

			// draw level 0
			int offset = 0;
			for (HierarchicalHsiSample s : hieraPalette) {
				paintColor(g, s.getCountOfLastLevelChildren() * colorWidth, offset * colorWidth, 0, s);
				offset += s.getCountOfLastLevelChildren();
			}
			
			offset = 0;
			for (HierarchicalHsiSample s : hieraPalette) {
				for (HierarchicalHsiSample s2 : s.getChildren()) {
					paintColor(g, s2.getCountOfLastLevelChildren() * colorWidth, offset * colorWidth, 1 * colorHeight, s2);
					offset += s2.getCountOfLastLevelChildren();				}
			}

			// draw level 1
			offset = 0;
			for (HierarchicalHsiSample s : hieraPalette) {
				for (HierarchicalHsiSample s2 : s.getChildren()) {
					for (HierarchicalHsiSample s3 : s2.getChildren()) {
						paintColor(g, colorWidth, offset++ * colorWidth, 2 * colorHeight, s3);
					}
				}
			}

		}

		private void paintColor(Graphics g, int width, int x, int y, Sample sample) {
			RgbSample rgbSample = ConversionService.toRgb(sample);
			Color c = new Color((int) rgbSample.getC1(), (int) rgbSample.getC2(), (int) rgbSample.getC3());
			g.setColor(c);
			g.fillRect(x, y, width, colorHeight);
		}

	}

}
