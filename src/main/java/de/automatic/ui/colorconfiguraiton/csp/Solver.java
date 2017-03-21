package de.automatic.ui.colorconfiguraiton.csp;

import java.util.ArrayList;

import org.eclipse.jdt.annotation.NonNullByDefault;

import com.analog.lyric.dimple.factorfunctions.core.FactorFunction;
import com.analog.lyric.dimple.model.core.FactorGraph;
import com.analog.lyric.dimple.model.domains.DiscreteDomain;
import com.analog.lyric.dimple.model.values.Value;
import com.analog.lyric.dimple.model.variables.Discrete;
import cern.colt.Arrays;
import de.automatic.ui.colorconfiguraiton.visualisation.ColorVarShower;

public class Solver {

	@NonNullByDefault
	public static class AccentColorFactor extends FactorFunction {
		@Override
		public final double evalEnergy(Value[] args) {

			ColorVar state1 = (ColorVar) args[0].getObject();

			return -Math.log(state1.getS()) + -Math.log(1.0 - state1.getRelativeHueGroupSize());

		}
	}

	public static void solve(ArrayList<ColorVar> vars) {

		// new ColorVarShower(vars.get(1));
		for (ColorVar v : vars) {
			System.out.println("HueGroup: " + v.getHueGroup());
			System.out.println("RelativeHueGroupSize: " + v.getRelativeHueGroupSize());
			System.out.println("Weight: " + v.getRelativeWeight());
		}

		FactorGraph graph = new FactorGraph();

		ColorVar[] domain = new ColorVar[vars.size()];
		vars.toArray(domain);

		Discrete accent = new Discrete(domain);
		graph.addFactor(new AccentColorFactor(), accent);

		graph.solve();

		new ColorVarShower((ColorVar) accent.getValue());
		// System.out.println(y.getValue());

	}

}
