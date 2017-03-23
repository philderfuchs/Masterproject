package de.automatic.ui.colorconfiguraiton.visualisation;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.automatic.ui.colorconfiguraiton.csp.ColorVar;
import de.automatic.ui.colorconfiguraiton.entities.RgbSample;
import de.automatic.ui.colorconfiguraiton.entities.Sample;
import de.automatic.ui.colorconfiguraiton.services.ConversionService;

public class ColorVarShower extends JFrame {

	private ColorVar var;
	private static int width = 200;
	private static int height = 200;

	public ColorVarShower(ColorVar var, String title, int x, int y) {
		super(title);
		this.var = var;
		this.add(new ColorPanel());

		this.setLocation(x, y);
		this.setSize(width, height);
		this.setVisible(true);

	}

	class ColorPanel extends JPanel {

		ColorPanel() {
			this.setOpaque(true);
		}

		public void paintComponent(Graphics g) {
			g.setColor(new Color(var.getR(), var.getG(), var.getB()));
			g.fillRect(0, 0, width, height);

		}

	}

}
