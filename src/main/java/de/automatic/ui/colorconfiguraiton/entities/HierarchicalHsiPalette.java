package de.automatic.ui.colorconfiguraiton.entities;

import java.util.ArrayList;

public class HierarchicalHsiPalette extends ArrayList<HierarchicalHsiSample> {

	public SampleList getSeeds() {
		GetSeedVisitor v = new GetSeedVisitor();
		for (HierarchicalHsiSample s : this) {
			s.accept(v);
		}
		return v.getSeeds();
	}

	public int getCountOfPixels() {
		int count = 0;
		for (HierarchicalHsiSample p : this) {
			count += p.getCount();
		}
		return count;
	}

}
