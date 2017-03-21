package scheming;

import de.automatic.ui.colorconfiguraiton.entities.HierarchicalHsiPalette;
import de.automatic.ui.colorconfiguraiton.entities.HierarchicalHsiSample;

public class PaletteAnalyzer {

	public static void analyze(HierarchicalHsiPalette palette) {
		
		System.out.println("Count of Hues: " + palette.size());
		for(HierarchicalHsiSample s : palette) {
			System.out.println("Weight: " + s.getWeight());
		}
		
	}
	
}
