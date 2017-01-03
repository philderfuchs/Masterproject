package de.automatic.ui.colorconfiguration.segmentation;

import de.automatic.ui.colorconfiguraiton.entities.SampleList;
import de.automatic.ui.colorconfiguraiton.entities.Channels;
import de.automatic.ui.colorconfiguraiton.entities.Histogram;
import de.automatic.ui.colorconfiguraiton.entities.Sample;

public class GreyCylinderFilterer {

	private SampleList filteredList;
	private SampleList greyCylinder;

	public GreyCylinderFilterer() {
		filteredList = new SampleList();
		greyCylinder = new SampleList();
	}

	public SampleList filterGreyCylinder(SampleList samples, double threshold) {
		for (int i = 0; i < samples.size(); i++) {
			if (samples.get(i).getC2() > threshold) {
				filteredList.add(samples.get(i));
			} else {
				greyCylinder.add(samples.get(i));
			}
		}
		return filteredList;
	}

	public void linkBackGreyCylinder(Histogram histo) {
		greyCylinder.sort(Channels.C1);

		double binRange = 1.0 / histo.size();
		int j = 0;

		for (int i = 0; i < histo.size() - 1; i++) {
			while (j < greyCylinder.size()
					&& greyCylinder.get(j).getNormalized(Channels.C1) <= (((double) i) + 1.0)) {
				histo.get(i).getSamples().add(greyCylinder.get(j));
				j++;
			}

		}
	}

}
