package de.automatic.ui.colorconfiguraiton.entities;

public class GetSeedVisitor implements Visitor {

	private SampleList seeds;

	public GetSeedVisitor() {
		this.seeds = new SampleList();
	}

	@Override
	public void visit(HierarchicalHsiSample sample) {
		if (sample.getChildren().size() == 0) {
			seeds.add(new HsiSample(sample.getC1(), sample.getC2(), sample.getC3(), sample.getCount()));
		}
	}

	public SampleList getSeeds() {
		return seeds;
	}

	public void setSeeds(SampleList seeds) {
		this.seeds = seeds;
	}

}
