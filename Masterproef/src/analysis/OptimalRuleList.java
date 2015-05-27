package analysis;

import genetic.Main;
import genetic.individuals.Evaluator;
import genetic.individuals.RangeCheck;
import genetic.individuals.rules.Condition;
import genetic.individuals.rules.Rule;
import genetic.individuals.rules.RuleList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import datasets.DataSet;
import datasets.stats.Features;

public class OptimalRuleList {

	public static RuleList get() {
		List<Rule> rules = new ArrayList<>();
		rules.add(getRule(5, 399.77, true, "SVM -C 0.1 -S 0"));
		rules.add(getRule(new int[] { 7, 1 }, new double[] { 0.762, 35.22 },
				new boolean[] { true, true }, "SVM -C 0.01 -S 4"));
		rules.add(getRule(0, 4853.62, false, "SVM -C 10 -S 6"));
		return new RuleList(rules, "NaiveBayes");
	}

	private static Rule getRule(int feature, double value, boolean isLower,
			String action) {
		return getRule(new int[] { feature }, new double[] { value },
				new boolean[] { isLower }, action);
	}

	private static Rule getRule(int[] features, double[] values,
			boolean[] isLowers, String action) {
		Collection<RangeCheck> checks = new ArrayList<>(features.length);
		for (int i = 0; i < features.length; i++)
			checks.add(new RangeCheck(features[i], values[i], isLowers[i]));
		return new Rule(new Condition(checks), action);
	}

	public static void main(String[] args) {
		Set<Features> fs = DataSet.testSets().map(Features::load)
				.collect(Collectors.toSet());
		System.out.println("Best solution accuracy = "
				+ Main.findBestSolution(fs));
		Evaluator eval = new Evaluator();
		RuleList optimal = get();
		double result = fs.stream()
				.mapToDouble(f -> optimal.apply(f).evaluate(f, eval)).average()
				.getAsDouble();
		System.out.println(optimal);
		System.out.println("Best rule list accuracy = " + result);
		RuleList genetic = Analyzer.defaultSettings().apply().getRules();
		double geneticResult = fs.stream()
				.mapToDouble(f -> genetic.apply(f).evaluate(f, eval)).average()
				.getAsDouble();
		//System.out.println(genetic);
		System.out.println("Genetic rule list accuracy = " + geneticResult);
	}
}
