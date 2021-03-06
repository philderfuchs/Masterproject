package de.automatic.ui.colorconfiguraiton.csp;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.analog.lyric.dimple.exceptions.DimpleException;
import com.analog.lyric.dimple.factorfunctions.core.FactorFunction;
import com.analog.lyric.dimple.model.core.FactorGraph;
import com.analog.lyric.dimple.model.domains.DiscreteDomain;
import com.analog.lyric.dimple.model.values.Value;
import com.analog.lyric.dimple.model.variables.Discrete;
import com.analog.lyric.dimple.options.BPOptions;
import com.analog.lyric.dimple.solvers.junctiontree.JunctionTreeSolver;

import de.automatic.ui.colorconfiguraiton.entities.GetCountOfLastOrderChildrenVisitor;
import de.automatic.ui.colorconfiguraiton.entities.HsiSample;
import de.automatic.ui.colorconfiguraiton.entities.RgbSample;
import de.automatic.ui.colorconfiguraiton.services.ConversionService;
import de.automatic.ui.colorconfiguraiton.visualisation.ColorVarShower;

public class Solver {

	public static final double contrastRatioThreshold = 4;

	public static class DifferentValueFactor extends FactorFunction {
		@Override
		public final double evalEnergy(Value[] args) {

			boolean allDifferent = true;
			for (int i = 0; i < args.length - 1; i++) {
				ColorVar state1 = (ColorVar) args[i].getObject();
				for (int j = i + 1; j < args.length; j++) {
					ColorVar state2 = (ColorVar) args[j].getObject();
					if (state1 == state2) {
						allDifferent = false;
					}
				}
			}
			return allDifferent ? 0 : Double.POSITIVE_INFINITY;
		}
	}

	public static class SameValueFactor extends FactorFunction {
		@Override
		public final double evalEnergy(Value[] args) {

			ColorVar state1 = (ColorVar) args[0].getObject();
			ColorVar state2 = (ColorVar) args[1].getObject();

			return state1 == state2 ? 0 : Double.POSITIVE_INFINITY;
		}
	}

	@NonNullByDefault
	public static class DifferentHueGroupFactor extends FactorFunction {
		@Override
		public final double evalEnergy(Value[] args) {

			boolean allDifferent = true;
			for (int i = 0; i < args.length - 1; i++) {
				ColorVar state1 = (ColorVar) args[i].getObject();
				for (int j = i + 1; j < args.length; j++) {
					ColorVar state2 = (ColorVar) args[j].getObject();
					if (state1.getHueGroup() == state2.getHueGroup()) {
						allDifferent = false;
					}
				}
			}
			return allDifferent ? 0 : Double.POSITIVE_INFINITY;
		}
	}

	@NonNullByDefault
	public static class AccentColorFactor extends FactorFunction {
		@Override
		public final double evalEnergy(Value[] args) {

			ColorVar state1 = (ColorVar) args[0].getObject();
			// return -Math.log(state1.getRelativeChroma()) + -Math.log(1.0 -
			// state1.getRelativeHueGroupSize());
			return -Math.log(state1.getS()) + -Math.log(1.0 - state1.getRelativeHueGroupSize());

		}
	}

	@NonNullByDefault
	public static class InteractionColorFactor extends FactorFunction {
		@Override
		public final double evalEnergy(Value[] args) {

			ColorVar state1 = (ColorVar) args[0].getObject();
			// return -Math.log(state1.getRelativeChroma());
			return -Math.log(state1.getS());
		}
	}

	@NonNullByDefault
	public static class PrimaryColorFactor extends FactorFunction {
		@Override
		public final double evalEnergy(Value[] args) {

			ColorVar state1 = (ColorVar) args[0].getObject();

			return -Math.log(state1.getRelativeWeight());

		}
	}

	@NonNullByDefault
	public static class SecondaryColorFactor extends FactorFunction {

		@Override
		public final double evalEnergy(Value[] args) {

			ColorVar primary = (ColorVar) args[0].getObject();
			ColorVar secondary = (ColorVar) args[1].getObject();

			double value = primary == secondary ? -Math.log(0.9) : 0.0;

			// return primary.getHueGroup() == secondary.getHueGroup()
			// ? -Math.log(1.0 - Math.abs(primary.getS() - secondary.getS())) :
			// Double.POSITIVE_INFINITY;
			return primary.getHueGroup() == secondary.getHueGroup() ? value : Double.POSITIVE_INFINITY;

		}
	}

	@NonNullByDefault
	public static class ContrastRatioFactor extends FactorFunction {

		@Override
		public final double evalEnergy(Value[] args) {

			ColorVar state1 = (ColorVar) args[0].getObject();
			ColorVar state2 = (ColorVar) args[1].getObject();

			Double lum1 = ContrastUtils.calculateLuminance(state1.getR(), state1.getG(), state1.getB());
			Double lum2 = ContrastUtils.calculateLuminance(state2.getR(), state2.getG(), state2.getB());

			Double contrastRatio = ContrastUtils.calculateContrastRatio(lum1, lum2);

			return contrastRatio >= contrastRatioThreshold ? -Math.log(contrastRatio / 22.0) : Double.POSITIVE_INFINITY;
		}
	}

	public static void solve(ArrayList<ColorVar> vars, double meanLuminance) {

		int countOfHueGroups = vars.get(vars.size() - 1).getHueGroup() + 1;
		System.out.println("Count of Hue Groups: " + countOfHueGroups);

		FactorGraph graph = new FactorGraph();
		graph.setOption(BPOptions.iterations, 50);
		graph.setSolverFactory(new JunctionTreeSolver());

		ColorVar[] domain = new ColorVar[vars.size()];
		for (int i = 0; i < vars.size(); i++) {
			printInfo(vars.get(i));
			domain[i] = vars.get(i);
		}
		DiscreteDomain domainWrapper = DiscreteDomain.create(domain);

		Discrete primary = new Discrete(domainWrapper);
		Discrete secondary = new Discrete(domainWrapper);
		Discrete accent = new Discrete(domainWrapper);
		Discrete interaction = new Discrete(domainWrapper);

		if (countOfHueGroups == 1) {
			
			System.out.println("--------- Case: 1 Hue Group ---------");

			graph.addFactor(new PrimaryColorFactor(), primary);
			graph.addFactor(new SecondaryColorFactor(), primary, secondary);

			graph.solve();

			ColorVar primaryColor = (ColorVar) primary.getValue();

			HsiSample compl = new HsiSample(primaryColor.getH(), primaryColor.getS(), primaryColor.getI(), 1);
			compl.setC1((compl.getC1() + 180.0) % 360.0);

			RgbSample rgb = ConversionService.toRgb(compl);
			ColorVar complColor = new ColorVar();
			complColor.setR((int) rgb.getC1());
			complColor.setG((int) rgb.getC2());
			complColor.setB((int) rgb.getC3());

			double delta = ContrastUtils.calculateLuminance(primaryColor.getR(), primaryColor.getG(),
					primaryColor.getB()) < 0.4 ? 0.05 : -0.05;

			do {
				System.out.println("move by " + delta);
				compl.setC3(compl.getC3() + delta);
				rgb = ConversionService.toRgb(compl);
				complColor.setR((int) rgb.getC1());
				complColor.setG((int) rgb.getC2());
				complColor.setB((int) rgb.getC3());

			} while (getContrastRatio(primaryColor, complColor) < contrastRatioThreshold);
			
			System.out.println("Compl Color: " + ConversionService.toHex(complColor.getR(), complColor.getG(), complColor.getB()));

			new ColorVarShower((ColorVar) primary.getValue(), "Primary", 300, 500);
			new ColorVarShower((ColorVar) secondary.getValue(), "Secondary", 500, 500);
			new ColorVarShower(complColor, "Accent", 700, 500);
			new ColorVarShower(complColor, "Interaction", 900, 500);

		} else if (countOfHueGroups == 2) {

			System.out.println("--------- Case: 2 Hue Groups ---------");

			graph.addFactor(new PrimaryColorFactor(), primary);

			graph.addFactor(new AccentColorFactor(), accent);
			graph.addFactor(new DifferentHueGroupFactor(), primary, accent);
			graph.addFactor(new ContrastRatioFactor(), primary, accent);

			graph.addFactor(new InteractionColorFactor(), interaction);

			// graph.addFactor(new DifferentHueGroupFactor(), accent,
			// interaction);
			graph.addFactor(new ContrastRatioFactor(), primary, interaction);
			graph.addFactor(new DifferentValueFactor(), primary, accent, interaction);

			graph.addFactor(new SecondaryColorFactor(), primary, secondary);
			graph.addFactor(new DifferentValueFactor(), secondary, accent);
			graph.addFactor(new DifferentValueFactor(), secondary, interaction);

			try {
				graph.solve();
			} catch (DimpleException e) {
				System.err.println("UNSAT");
			}

			new ColorVarShower((ColorVar) primary.getValue(), "Primary", 300, 500);
			new ColorVarShower((ColorVar) secondary.getValue(), "Secondary", 500, 500);
			new ColorVarShower((ColorVar) accent.getValue(), "Accent", 700, 500);
			new ColorVarShower((ColorVar) interaction.getValue(), "Interaction", 900, 500);

		} else {

			System.out.println("--------- Case: 3 Hue Groups ---------");

			graph.addFactor(new PrimaryColorFactor(), primary);

			graph.addFactor(new AccentColorFactor(), accent);
			graph.addFactor(new ContrastRatioFactor(), primary, accent);

			graph.addFactor(new InteractionColorFactor(), interaction);

			graph.addFactor(new DifferentHueGroupFactor(), primary, accent, interaction);
			graph.addFactor(new ContrastRatioFactor(), primary, interaction);

			graph.addFactor(new SecondaryColorFactor(), primary, secondary);

			try {
				graph.solve();
			} catch (DimpleException e) {
				System.err.println("UNSAT at Secondary");
			}

			new ColorVarShower((ColorVar) primary.getValue(), "Primary", 300, 500);
			new ColorVarShower((ColorVar) secondary.getValue(), "Secondary", 500, 500);
			new ColorVarShower((ColorVar) accent.getValue(), "Accent", 700, 500);
			new ColorVarShower((ColorVar) interaction.getValue(), "Interaction", 900, 500);

		}

		ColorVar background = new ColorVar();
		if (meanLuminance <= 0.2) {
			background.setR(0);
			background.setG(0);
			background.setB(0);
		} else {
			background.setR(255);
			background.setG(255);
			background.setB(255);
		}
		new ColorVarShower(background, "Background", 1100, 500);

	}

	private static void setByRank(int rank, Discrete y, ColorVar[] domain) {
		double[] belief = y.getBelief();
		Arrays.sort(belief);
		double valueAtRank = belief[belief.length - rank];

		for (int i = 0; i < y.getBelief().length; i++) {
			if (valueAtRank == y.getBelief()[i]) {
				y.setFixedValue(domain[i]);
			}
		}
	}

	private static void printInfo(ColorVar c) {
		System.out.println(ConversionService.toHex(c.getR(), c.getG(), c.getB()) + " | Rel. Chroma: "
				+ c.getRelativeChroma() + " | Saturation: " + c.getS());
	}

	private static Double getContrastRatio(ColorVar state1, ColorVar state2) {
		Double lum1 = ContrastUtils.calculateLuminance(state1.getR(), state1.getG(), state1.getB());
		Double lum2 = ContrastUtils.calculateLuminance(state2.getR(), state2.getG(), state2.getB());

		Double contrastRatio = ContrastUtils.calculateContrastRatio(lum1, lum2);
		return contrastRatio;
	}
}
