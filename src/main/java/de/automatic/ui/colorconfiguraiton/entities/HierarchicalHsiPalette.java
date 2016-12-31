package de.automatic.ui.colorconfiguraiton.entities;

public class HierarchicalHsiPalette extends HsiSample {

	SampleList children;
	SampleList modeSamples;

	public HierarchicalHsiPalette(double h, double s, double i, int count, SampleList children, SampleList samples) {
		super(h, s, i, count);
		this.children = children;
		this.modeSamples = samples;
	}

	public HierarchicalHsiPalette(double h, double s, double i, int count) {
		super(h, s, i, count);
		this.children = new SampleList();
		this.modeSamples = new SampleList();
	}

	public HierarchicalHsiPalette(HsiSample p, SampleList children, SampleList samples) {
		super(p.get(Channels.C1), p.get(Channels.C2), p.get(Channels.C3), p.getCount());
		this.children = children;
		this.modeSamples = samples;
	}

	public HierarchicalHsiPalette(HsiSample p) {
		super(p.get(Channels.C1), p.get(Channels.C2), p.get(Channels.C3), p.getCount());
		this.children = new SampleList();
		this.modeSamples = new SampleList();
	}

	public HierarchicalHsiPalette(Sample p) {
		super(((HsiSample) p).get(Channels.C1), ((HsiSample) p).get(Channels.C2), ((HsiSample) p).get(Channels.C3),
				((HsiSample) p).getCount());
		this.children = new SampleList();
		this.modeSamples = new SampleList();
	}

	public HierarchicalHsiPalette(Sample p, SampleList samples) {
		super(((HsiSample) p).get(Channels.C1), ((HsiSample) p).get(Channels.C2), ((HsiSample) p).get(Channels.C3),
				((HsiSample) p).getCount());
		this.children = new SampleList();
		this.modeSamples = samples;
	}

	public SampleList getChildren() {
		return children;
	}

	public void setChildren(SampleList children) {
		this.children = children;
	}

	public SampleList getModeSamples() {
		return modeSamples;
	}

	public void setModeSamples(SampleList samples) {
		this.modeSamples = samples;
	}

}
