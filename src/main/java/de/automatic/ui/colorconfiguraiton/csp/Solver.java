package de.automatic.ui.colorconfiguraiton.csp;

import java.util.ArrayList;

import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.analog.lyric.dimple.factorfunctions.core.FactorFunction;
import com.analog.lyric.dimple.model.core.FactorGraph;
import com.analog.lyric.dimple.model.domains.DiscreteDomain;
import com.analog.lyric.dimple.model.factors.Factor;
import com.analog.lyric.dimple.model.values.Value;
import com.analog.lyric.dimple.model.variables.Discrete;
import com.analog.lyric.dimple.options.BPOptions;
import com.analog.lyric.dimple.solvers.gibbs.GibbsSolver;
import com.analog.lyric.dimple.solvers.junctiontree.JunctionTreeSolver;
import com.analog.lyric.dimple.solvers.junctiontreemap.JunctionTreeMAPSolver;
import com.analog.lyric.dimple.solvers.minsum.MinSumSolver;
import com.analog.lyric.dimple.solvers.particleBP.ParticleBPSolver;
import com.analog.lyric.dimple.exceptions.DimpleException;
import com.chroma.Chroma;
import com.chroma.ColorSpace;

import de.automatic.ui.colorconfiguraiton.entities.RgbSample;
import de.automatic.ui.colorconfiguraiton.services.ConversionService;
import de.automatic.ui.colorconfiguraiton.visualisation.ColorVarShower;

public class Solver {

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

			// return primary.getHueGroup() == secondary.getHueGroup()
			// ? -Math.log(1.0 - Math.abs(primary.getS() - secondary.getS())) :
			// Double.POSITIVE_INFINITY;
			return primary.getHueGroup() == secondary.getHueGroup() ? 0.0 : Double.POSITIVE_INFINITY;

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

			// System.out.println(contrastRatio);

			return contrastRatio >= 2.4 ? -Math.log(contrastRatio / 22.0) : Double.POSITIVE_INFINITY;
		}
	}

	public static void solve(ArrayList<ColorVar> vars) {

		int countOfHueGroups = vars.get(vars.size() - 1).getHueGroup() + 1;
		System.out.println("Count of Hue Groups: " + countOfHueGroups);

		ColorVar[] domain = new ColorVar[vars.size()];
		for (int i = 0; i < vars.size(); i++) {
			printInfo(vars.get(i));
			domain[i] = vars.get(i);
		}
		DiscreteDomain domainWrapper = DiscreteDomain.create(domain);

		boolean sat = false;

		Discrete primary = new Discrete(domainWrapper);
		Discrete secondary = new Discrete(domainWrapper);
		Discrete accent = new Discrete(domainWrapper);
		Discrete interaction = new Discrete(domainWrapper);

		int primaryRank = 1;

		do {
			if (primaryRank == domain.length) {
				System.out.println("No Solution Found");
				break;
			}

			FactorGraph graph = new FactorGraph();
			graph.setOption(BPOptions.iterations, 50);
			// graph.setSolverFactory(new JunctionTreeSolver());

			primary = new Discrete(domainWrapper);
			secondary = new Discrete(domainWrapper);
			accent = new Discrete(domainWrapper);
			interaction = new Discrete(domainWrapper);

			Factor primaryFactor = graph.addFactor(new PrimaryColorFactor(), primary);

			graph.solve();
			primary.setFixedValue(primary.getValue());

			// setByRank(primaryRank, primary, domain);

			Factor accentFactor = graph.addFactor(new AccentColorFactor(), accent);
			Factor paFactor1 = graph.addFactor(new DifferentHueGroupFactor(), primary, accent);
			Factor paFactor2 = graph.addFactor(new ContrastRatioFactor(), primary, accent);
			Factor paJoined = graph.join(paFactor1, paFactor2);

			try {
				graph.solve();
			} catch (DimpleException e) {
				System.out.println("UNSAT at Accent");
				break;
			}
			accent.setFixedValue(accent.getValue());

			Factor interactionFactor = graph.addFactor(new InteractionColorFactor(), interaction);

			if (countOfHueGroups < 3) {
				// only two hue groups
				Factor aiFactor1 = graph.addFactor(new DifferentHueGroupFactor(), accent, interaction);
				Factor piFactor1 = graph.addFactor(new ContrastRatioFactor(), primary, interaction);
				Factor piFactor2 = graph.addFactor(new DifferentValueFactor(), primary, interaction);
				Factor piJoined = graph.join(piFactor1, piFactor2);
				graph.join(aiFactor1, piJoined, paJoined);

			} else {
				Factor paiFactor = graph.addFactor(new DifferentHueGroupFactor(), primary, accent, interaction);
				Factor piFactor = graph.addFactor(new ContrastRatioFactor(), primary, interaction);
//				graph.join(paiFactor, piFactor);
			}

			try {
				graph.solve();
			} catch (DimpleException e) {
				System.out.println("UNSAT at Interaction");
				break;
			}

			interaction.setFixedValue(interaction.getValue());

			if (((ColorVar) primary.getFixedValue()).getHueGroupSize() > 1) {

				Factor secondaryFactor = graph.addFactor(new SecondaryColorFactor(), primary, secondary);
				Factor primSecDiffFactor = graph.addFactor(new DifferentValueFactor(), primary, secondary, accent,
						interaction);
				graph.join(secondaryFactor, primSecDiffFactor);
			} else {
				graph.addFactor(new SameValueFactor(), primary, secondary);
			}

			try {
				graph.solve();
			} catch (DimpleException e) {
				System.out.println("UNSAT at Secondary");
				secondary.setFixedValue(primary.getValue());
				break;
			}
			secondary.setFixedValue(secondary.getValue());

			sat = true;

		} while (!sat);

		System.out.println("Primary: " + Arrays.toString(primary.getBelief()));
		System.out.println("Secondary: " + Arrays.toString(secondary.getBelief()));
		System.out.println("Accent: " + Arrays.toString(accent.getBelief()));
		System.out.println("Interaction: " + Arrays.toString(interaction.getBelief()));
		new ColorVarShower((ColorVar) primary.getValue(), "Primary", 500, 500);
		new ColorVarShower((ColorVar) secondary.getValue(), "Secondary", 700, 500);
		new ColorVarShower((ColorVar) accent.getValue(), "Accent", 900, 500);
		new ColorVarShower((ColorVar) interaction.getValue(), "Interaction", 1100, 500);
		// new ColorVarShower(vars.get(3), "yo", 900, 500);

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
}
