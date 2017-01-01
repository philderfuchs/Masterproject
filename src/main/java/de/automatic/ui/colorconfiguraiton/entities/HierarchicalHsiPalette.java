package de.automatic.ui.colorconfiguraiton.entities;

import java.util.ArrayList;

public class HierarchicalHsiPalette extends ArrayList<HierarchicalHsiSample> {

	public SampleList getSeeds() {
		GetSeedsVisitor v = new GetSeedsVisitor();
		for (HierarchicalHsiSample s : this) {
			s.accept(v);
		}
		return v.getSeeds();
	}
	
	public int getCountOfLastLevelChildren() {
		GetCountOfLastOrderChildrenVisitor v = new GetCountOfLastOrderChildrenVisitor();
		for (HierarchicalHsiSample s : this) {
			s.accept(v);
		}
		return v.getCount();
	}

	public int getCountOfPixels() {
		int count = 0;
		for (HierarchicalHsiSample p : this) {
			count += p.getCount();
		}
		return count;
	}

}
