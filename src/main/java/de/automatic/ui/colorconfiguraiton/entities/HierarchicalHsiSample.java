package de.automatic.ui.colorconfiguraiton.entities;

public class HierarchicalHsiSample extends HsiSample implements Visitable {

	HierarchicalHsiPalette children;
	SampleList modeSamples;

	public HierarchicalHsiSample(double h, double s, double i, int count, HierarchicalHsiPalette children, SampleList samples) {
		super(h, s, i, count);
		this.children = children;
		this.modeSamples = samples;
	}

	public HierarchicalHsiSample(double h, double s, double i, int count) {
		super(h, s, i, count);
		this.children = new HierarchicalHsiPalette();
		this.modeSamples = new SampleList();
	}

	public HierarchicalHsiSample(HsiSample p, HierarchicalHsiPalette children, SampleList samples) {
		super(p.get(Channels.C1), p.get(Channels.C2), p.get(Channels.C3), p.getCount());
		this.children = children;
		this.modeSamples = samples;
	}

	public HierarchicalHsiSample(HsiSample p) {
		super(p.get(Channels.C1), p.get(Channels.C2), p.get(Channels.C3), p.getCount());
		this.children = new HierarchicalHsiPalette();
		this.modeSamples = new SampleList();
	}

	public HierarchicalHsiSample(Sample p) {
		super(((HsiSample) p).get(Channels.C1), ((HsiSample) p).get(Channels.C2), ((HsiSample) p).get(Channels.C3),
				((HsiSample) p).getCount());
		this.children = new HierarchicalHsiPalette();
		this.modeSamples = new SampleList();
	}

	public HierarchicalHsiSample(Sample p, SampleList samples) {
		super(((HsiSample) p).get(Channels.C1), ((HsiSample) p).get(Channels.C2), ((HsiSample) p).get(Channels.C3),
				((HsiSample) p).getCount());
		this.children = new HierarchicalHsiPalette();
		this.modeSamples = samples;
	}

	public HierarchicalHsiPalette getChildren() {
		return children;
	}

	public void setChildren(HierarchicalHsiPalette children) {
		this.children = children;
	}

	public SampleList getModeSamples() {
		return modeSamples;
	}

	public void setModeSamples(SampleList samples) {
		this.modeSamples = samples;
	}

	@Override
	public void accept(Visitor v) {
		
		v.visit(this);
		for(HierarchicalHsiSample s : children) {
			s.accept(v);
		}
		
	}

}
