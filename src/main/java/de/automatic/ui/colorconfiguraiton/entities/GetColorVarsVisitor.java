package de.automatic.ui.colorconfiguraiton.entities;

import java.util.ArrayList;

import de.automatic.ui.colorconfiguraiton.csp.ColorVar;
import de.automatic.ui.colorconfiguraiton.services.ConversionService;

public class GetColorVarsVisitor implements Visitor {
	
	private ArrayList<ColorVar> vars;

	public GetColorVarsVisitor() {
		this.vars = new ArrayList<ColorVar>();
	}

	@Override
	public void visit(HierarchicalHsiSample sample) {
		if (sample.getChildren().size() == 0) {
			ColorVar var = new ColorVar();
			var.setH(sample.getC1());
			var.setS(sample.getC2());
			var.setI(sample.getC3());
			
			RgbSample rgb = ConversionService.toRgb(sample.getC1(), sample.getC2(), sample.getC3(), 1);
			var.setR((int) rgb.getC1());
			var.setG((int) rgb.getC2());
			var.setB((int) rgb.getC3());
			vars.add(var);
		}
	}

	public ArrayList<ColorVar> getVars() {
		return vars;
	}

	public void setVars(ArrayList<ColorVar> vars) {
		this.vars = vars;
	}

}
