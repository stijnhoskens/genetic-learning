package genetic.individuals.rules;

import static java.lang.Math.max;
import static java.lang.Math.min;
import genetic.individuals.RangeCheck;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.function.IntSupplier;

import learning.Classifiers;
import datasets.stats.FeatureStatistics;

public class RandomSupplier {

	private static final Random r = new Random();

	public static RangeCheck rangeCheck(int nbOfBounds) {
		FeatureStatistics stats = FeatureStatistics.get();
		int index = r.nextInt(stats.nbOfFeatures());
		double bound = stats.getSample(index);
		if (nbOfBounds == 1)
			return new RangeCheck(index, bound, r.nextBoolean());
		double secondBound = stats.getSample(index);
		return new RangeCheck(index, min(bound, secondBound), max(bound,
				secondBound));
	}

	public static Condition condition(int nbOfRangeChecks, IntSupplier bounds) {
		Collection<RangeCheck> checks = new ArrayList<>();
		for (int j = 0; j < nbOfRangeChecks; j++) {
			int nbOfBounds = bounds.getAsInt();
			RangeCheck check = rangeCheck(nbOfBounds);
			checks.add(check);
		}
		return new Condition(checks);
	}

	public static Rule rule(int nbOfRangeChecks, IntSupplier bounds) {
		return new Rule(condition(nbOfRangeChecks, bounds), classifier());
	}

	public static String classifier() {
		return Classifiers.randomClassifier();
	}

	public static RuleList ruleList(int nbOfRules, IntSupplier rangeChecks,
			IntSupplier bounds) {
		List<Rule> rules = new ArrayList<>();
		for (int i = 0; i < nbOfRules; i++)
			rules.add(rule(rangeChecks.getAsInt(), bounds));
		return new RuleList(rules);
	}
}
