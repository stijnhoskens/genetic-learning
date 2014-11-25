package genetic.init;

import genetic.Config;
import genetic.Population;
import genetic.individuals.Evaluator;
import genetic.individuals.rules.RandomSupplier;
import genetic.individuals.rules.RuleList;
import genetic.individuals.rules.RuledIndividual;

import java.util.Properties;
import java.util.Set;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;

import util.Statistics;
import datasets.DataSet;
import datasets.stats.Features;

public class RandomGenerator implements IndividualGenerator<RuledIndividual> {

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
		IntSupplier rangeChecks = () -> Statistics.getChanceIndex(prop
				.getProperty("nbOfRangeChecks")) + 1;
		IntSupplier bounds = () -> Statistics.getChanceIndex(prop
				.getProperty("1vs2bounds")) + 1;
		RuleList rules = RandomSupplier
				.ruleList(nbOfRules, rangeChecks, bounds);
		return new RuledIndividual(feat, rules, eval);
	}

	public static void main(String[] args) {
		Set<Features> features = DataSet.trainingSets().map(Features::load)
				.collect(Collectors.toSet());
		Evaluator eval = new Evaluator();
		System.out.println(new Population<>(
				new RandomGenerator(features, eval), 10));
	}
}
