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

	private HierarchicalHsiPalette hieraPalette;
	private int windowLength = 400;
	private int windowHeight = 400;
	private int x;
	private int y;
	String title;

	public HierarchicalPaletteShower(HierarchicalHsiPalette hieraPalette, String title, int x, int y) {
		this.hieraPalette = hieraPalette;
		this.title = title;
		this.x = x;
		this.y = y;
		this.visualizePalette();
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
			int countOfChildren = 0;

			int lengthOfQuader = windowLength / hieraPalette.size();
			int x = 0;
			for (Sample sample : hieraPalette) {
				countOfChildren += ((HierarchicalHsiSample) sample).getChildren().size();
				paintColor(g, lengthOfQuader, x++, 0, sample);
			}
			lengthOfQuader = windowLength / countOfChildren;
			x = 0;
			for (Sample sample : hieraPalette) {
				for (Sample child : ((HierarchicalHsiSample) sample).getChildren()) {
					paintColor(g, lengthOfQuader, x++, 200, child);
				}
			}
		}

		private void paintColor(Graphics g, int lengthOfQuader, int x, int y, Sample sample) {
			RgbSample rgbSample = ConversionService.toRgb(sample);
			Color c = new Color((int) rgbSample.getC1(), (int) rgbSample.getC2(), (int) rgbSample.getC3());
			g.setColor(c);
			g.fillRect(x * lengthOfQuader, y, lengthOfQuader, windowHeight);
		}

	}

}
