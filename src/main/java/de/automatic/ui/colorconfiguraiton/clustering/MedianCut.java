package de.automatic.ui.colorconfiguraiton.clustering;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import de.automatic.ui.colorconfiguraiton.entities.Channel;
import de.automatic.ui.colorconfiguraiton.entities.Cluster;
import de.automatic.ui.colorconfiguraiton.entities.Cube;
import de.automatic.ui.colorconfiguraiton.entities.Histogram;
import de.automatic.ui.colorconfiguraiton.entities.Pixel;

public class MedianCut implements StepByStepClusterer {

	private ArrayList<Cluster> cubes;

	public MedianCut() {
		cubes = new ArrayList<Cluster>();
	}

	public ArrayList<Cluster> step(Histogram histogram) {

		if (cubes.size() == 0) {
			Cube initialCube = new Cube(histogram);
			cubes.add(initialCube);
			return cubes;
		} else {

			Cube cube = (Cube) this.getBiggestCube(cubes);
			cubes.remove(cube);

			sortHistogram(cube, cube.getLongestDistance());

			float totalCount = cube.getHistogram().getCountOfPixels();
			Histogram histogramOfChildCube1 = new Histogram();
			Histogram histogramOfChildCube2 = new Histogram();

			float currentCount = 0;
			for (Pixel p : cube.getHistogram().getPixelList()) {
				currentCount += p.getCount();
				if (currentCount <= (totalCount / 2.0f) || histogramOfChildCube1.getLength() == 0) {
					histogramOfChildCube1.add(p);
				} else {
					histogramOfChildCube2.add(p);
				}
			}

			Cube childCube1 = new Cube(histogramOfChildCube1);
			Cube childCube2 = new Cube(histogramOfChildCube2);

			cubes.add(childCube1);
			cubes.add(childCube2);
		}
		return cubes;
	}

	private void sortHistogram(Cube cube, Channel colorWithLongestDistance) {
		switch (colorWithLongestDistance) {
		case R:
			cube.getHistogram().sort(Channel.R);
			break;
		case G:
			cube.getHistogram().sort(Channel.G);
			break;
		case B:
			cube.getHistogram().sort(Channel.B);
			break;
		default:
			break;
		}
	}

	private Cluster getBiggestCube(ArrayList<Cluster> cubeList) {
		Collections.sort(cubeList);
		return cubeList.get(0);
	}

	public ArrayList<Cluster> getCubes() {
		return cubes;
	}

	public void setCubes(ArrayList<Cluster> cubes) {
		this.cubes = cubes;
	}

}
