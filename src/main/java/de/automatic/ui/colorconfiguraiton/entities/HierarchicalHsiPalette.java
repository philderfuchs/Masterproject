package de.automatic.ui.colorconfiguraiton.entities;

import java.util.ArrayList;

import de.automatic.ui.colorconfiguraiton.csp.ColorVar;

public class HierarchicalHsiPalette extends ArrayList<HierarchicalHsiSample> {

	public SampleList getSeeds() {
		GetSeedsVisitor v = new GetSeedsVisitor();
		for (HierarchicalHsiSample s : this) {
			s.accept(v);
		}
		return v.getSeeds();
	}
	
	public ArrayList<ColorVar> getColorVars() {
				
		int totalWeights = 0;
		for (HierarchicalHsiSample s : this) {
			totalWeights += s.getWeight();
		}
		GetColorVarsVisitor v = new GetColorVarsVisitor(totalWeights, this.getCountOfLastLevelChildren());
		for(int i = 0; i < this.size(); i++) {
			v.setCurrentHueGroup(i);
			v.setCurrentHueGroupSize(this.get(i).getCountOfLastLevelChildren());
			this.get(i).accept(v);
			
		}
		return v.getVars();
	}
	
	public int getCountOfLastLevelChildren() {
		GetCountOfLastOrderChildrenVisitor v = new GetCountOfLastOrderChildrenVisitor();
		for (HierarchicalHsiSample s : this) {
			s.accept(v);
		}
		return v.getCount();
	}

	public int getCurrentLevelCountOfSamples() {
		int count = 0;
		for (HierarchicalHsiSample p : this) {
			count += p.getCount();
		}
		return count;
	}
	
	public int getLastLevelCountOfSamples() {
		GetSeedsVisitor v = new GetSeedsVisitor();
		for (HierarchicalHsiSample s : this) {
			s.accept(v);
		}
		return v.getTotalCount();
	}

}
