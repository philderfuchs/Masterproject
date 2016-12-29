package de.automatic.ui.colorconfiguration.segmentation;

import de.automatic.ui.colorconfiguraiton.entities.Histogram;

public interface StatisticalTest {
	
	public boolean similiar(Histogram histo1, Histogram histo2, int start, int end);
	
}
