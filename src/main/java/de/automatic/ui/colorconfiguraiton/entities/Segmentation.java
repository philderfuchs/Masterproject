package de.automatic.ui.colorconfiguraiton.entities;

import java.util.ArrayList;

public class Segmentation extends ArrayList<Double> {

	private Channels channel;

	public Segmentation(Channels channel) {
		super();
		this.channel = channel;
	}

	public Channels getChannel() {
		return channel;
	}

	public void setChannel(Channels channel) {
		this.channel = channel;
	}
	
	
	
}
