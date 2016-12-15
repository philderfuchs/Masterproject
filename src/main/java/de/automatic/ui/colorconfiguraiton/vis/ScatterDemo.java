package de.automatic.ui.colorconfiguraiton.vis;

import java.util.Random;

import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import de.automatic.ui.colorconfiguraiton.entities.Histogram;
import de.automatic.ui.colorconfiguraiton.entities.Pixel;
import de.automatic.ui.colorconfiguraiton.entities.Channel;

public class ScatterDemo extends AbstractAnalysis {

	private Histogram histogram;

	public ScatterDemo(Histogram histogram) throws Exception {
		this.histogram = histogram;
		AnalysisLauncher.open(this);
	}

	public void init() {
		int size = histogram.getLength();
		float x;
		float y;
		float z;
		float a;

		Coord3d[] points = new Coord3d[size];
		Color[] colors = new Color[size];

		Random r = new Random();
		r.setSeed(0);

		int i = 0;
		for (Pixel p : histogram.getPixelList()) {
			x = ((float) p.get(Channel.R) / 255.0f) - 0.5f;
			y = ((float) p.get(Channel.G) / 255.0f) - 0.5f;
			z = ((float) p.get(Channel.B) / 255.0f) - 0.5f;
			points[i] = new Coord3d(x, y, z);
			a = 0.25f;
			colors[i++] = new Color(x, y, z, a);
		}

		Scatter scatter = new Scatter(points, colors);
		chart = AWTChartComponentFactory.chart(Quality.Advanced, "newt");
		chart.getScene().add(scatter);
	}
}