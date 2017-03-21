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
				int state1 = (int) args[0].getObject();
				int state2 = (int) args[1].getObject();

				int diff = state1 - state2;

				return diff >= 2 ? 0 : Double.POSITIVE_INFINITY;
			}
		}

		public static void solve(ArrayList<ColorVar> vars) {
			
			new ColorVarShower(vars.get(1));
			
			FactorGraph graph = new FactorGraph();
			
			ColorVar[] domain = new ColorVar[vars.size()];
			vars.toArray(domain);
//			System.out.println(domain[0].getH());

//			DiscreteDomain domain = DiscreteDomain.create(1, 2, 3);
//			Discrete accent = new Discrete(domain);
//			graph.addFactor(new AccentColorFactor());
//
//			graph.solve();
//
//			System.out.println(x.getValue());
//			System.out.println(y.getValue());

		}

	}
	

