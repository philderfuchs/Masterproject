package de.automatic.ui.colorconfiguration.segmentation;

import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

import de.automatic.ui.colorconfiguraiton.entities.Histogram;

public class weikerTTest implements StatisticalTest {
	
	public boolean similiar(Histogram histo1, Histogram histo2, int start, int end) {
		SummaryStatistics stats = new SummaryStatistics();
		for (int i = start; i <= end; i++) {
			double valueToAdd = Math.abs(histo1.get(i).getValue() - histo2.get(i).getValue());
			stats.addValue(valueToAdd);
		}
		if (stats.getMean() == 0 || stats.getStandardDeviation() == 0) {
			// System.out.println("0 Case");
			return true;
		}
		double t = Math.sqrt(stats.getN()) * stats.getMean() / stats.getStandardDeviation();
		double criticalT = new TDistribution(end - start).inverseCumulativeProbability(0.97);
		return t < criticalT;
	}
	
}
