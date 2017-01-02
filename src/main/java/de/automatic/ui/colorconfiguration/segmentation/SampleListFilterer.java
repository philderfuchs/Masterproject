package de.automatic.ui.colorconfiguration.segmentation;

import de.automatic.ui.colorconfiguraiton.entities.SampleList;
import de.automatic.ui.colorconfiguraiton.entities.Channels;
import de.automatic.ui.colorconfiguraiton.entities.Histogram;
import de.automatic.ui.colorconfiguraiton.entities.Sample;

public class SampleListFilterer {

	private SampleList filteredList;
	private SampleList greyCylinder;

	public SampleListFilterer() {
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
		
		int j = 0;

		for (int i = 0; i < histo.size() -1 ; i++) {
			while (j < greyCylinder.size() && greyCylinder.get(j).getNormalized(Channels.C1) <= histo.get(i + 1).getKey()) {
				histo.get(i).getSamples().add(greyCylinder.get(j));
				j++;
			}

		}
	}

//	public SampleList getFilteredSamples() {
//		return filteredSamples;
//	}
//
//	public void setFilteredSamples(SampleList filteredSamples) {
//		this.filteredSamples = filteredSamples;
//	}

}
