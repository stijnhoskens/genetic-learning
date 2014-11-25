package genetic.init;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import learning.Classifiers;
import datasets.DataSet;
import datasets.stats.FeatureStatistics;
import datasets.stats.Features;
import util.Statistics;
import genetic.Config;
import genetic.Population;
import genetic.individuals.Evaluator;
import genetic.individuals.RangeCheck;
import genetic.individuals.rules.Condition;
import genetic.individuals.rules.Rule;
import genetic.individuals.rules.RuleList;
import genetic.individuals.rules.RuledIndividual;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class RandomGenerator implements IndividualGenerator<RuledIndividual> {

	private final Random r = new Random();
	private final Set<Features> feat;
	private final Evaluator eval;

	public RandomGenerator(Set<Features> features, Evaluator evaluator) {
		feat = features;
		eval = evaluator;
	}

	@Override
	public RuledIndividual get() {
		Properties prop = Config.INIT;
		int nbOfRules = Statistics
				.getChanceIndex(prop.getProperty("nbOfRules")) + 1;
		List<Rule> rules = new ArrayList<>();
		for (int i = 0; i < nbOfRules; i++) {
			int nbOfRangeChecks = Statistics.getChanceIndex(prop
					.getProperty("nbOfRangeChecks")) + 1;
			Collection<RangeCheck> checks = new ArrayList<>();
			for (int j = 0; j < nbOfRangeChecks; j++) {
				int nbOfBounds = Statistics.getChanceIndex(prop
						.getProperty("1vs2bounds")) + 1;
				RangeCheck check = getRangeCheck(nbOfBounds);
				checks.add(check);
			}
			rules.add(new Rule(new Condition(checks), Classifiers
					.randomClassifier()));
		}
		return new RuledIndividual(feat, new RuleList(rules), eval);
	}

	private RangeCheck getRangeCheck(int nbOfBounds) {
		FeatureStatistics stats = FeatureStatistics.get();
		int index = r.nextInt(stats.nbOfFeatures());
		double bound = stats.getSample(index);
		if (nbOfBounds == 1)
			return new RangeCheck(index, bound, r.nextBoolean());
		double secondBound = stats.getSample(index);
		return new RangeCheck(index, min(bound, secondBound), max(bound,
				secondBound));
	}

	public static void main(String[] args) {
		Set<Features> features = DataSet.trainingSets().map(Features::load)
				.collect(Collectors.toSet());
		Evaluator eval = new Evaluator();
		System.out.println(new Population<>(
				new RandomGenerator(features, eval), 10));
	}
}
