package de.automatic.ui.colorconfiguraiton.entities;

import java.util.ArrayList;

/**
 * Saves the indexes of the corresponding histogram which build modes.
 *  *
 */
public class Segmentation extends ArrayList<Integer> {

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
