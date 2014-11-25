package genetic.mutation;

import genetic.Config;
import genetic.individuals.Evaluator;
import genetic.individuals.rules.Condition;
import genetic.individuals.rules.RandomSupplier;
import genetic.individuals.rules.Rule;
import genetic.individuals.rules.RuledIndividual;
import genetic.init.RandomGenerator;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.function.IntSupplier;
import java.util.stream.Collectors;

import datasets.DataSet;
import datasets.stats.Features;
import util.Statistics;

public class Mutator implements MutationStrategy<RuledIndividual> {

	private final Random r = new Random();

	@Override
	public RuledIndividual mutate(RuledIndividual individual) {
		Properties prop = Config.MUTATION;
		int i = Statistics.getChanceIndex(prop.getProperty("typeOfMutation"));
		return Arrays
				.asList(condition(), action(), swap(), removal(), addition())
				.get(i).mutate(individual);
	}

	private static List<Rule> getRules(RuledIndividual i) {
		return i.getRules().asList();
	}

	private static void swapRule(List<Rule> rules, int i, Rule newRule) {
		rules.remove(i);
		rules.add(i, newRule);
	}

	private static void swapRules(List<Rule> rules, int i, int j) {
		Rule ruleI = rules.get(i);
		Rule ruleJ = rules.get(j);
		swapRule(rules, i, ruleJ);
		swapRule(rules, j, ruleI);
	}

	private MutationStrategy<RuledIndividual> condition() {
		return i -> {
			List<Rule> rules = getRules(i);
			if (rules.size() == 1)
				return i;
			int index = r.nextInt(rules.size() - 1);
			Rule rule = rules.get(index);
			Properties prop = Config.INIT;
			int nbOfRangeChecks = Statistics.getChanceIndex(prop
					.getProperty("nbOfRangeChecks")) + 1;
			IntSupplier bounds = () -> Statistics.getChanceIndex(prop
					.getProperty("1vs2bounds")) + 1;
			Condition condition = RandomSupplier.condition(nbOfRangeChecks,
					bounds);
			Rule newRule = rule.withNewCondition(condition);
			swapRule(rules, index, newRule);
			return i.withNewRules(rules);
		};
	}

	// TODO implement a way to prefer changing parameters over the model.

	private MutationStrategy<RuledIndividual> action() {
		return i -> {
			List<Rule> rules = getRules(i);
			int index = r.nextInt(rules.size());
			Rule rule = rules.get(index);
			Rule newRule = rule.withNewAction(RandomSupplier.classifier());
			swapRule(rules, index, newRule);
			return i.withNewRules(rules);
		};
	}

	// TODO implement a rule-fitness to chance distribution

	private MutationStrategy<RuledIndividual> swap() {
		return i -> {
			List<Rule> rules = getRules(i);
			if (rules.size() == 1)
				return i;
			int j = r.nextInt(rules.size() - 1);
			int k = r.nextInt(rules.size() - 1);
			swapRules(rules, j, k);
			return i.withNewRules(rules);
		};
	}

	private MutationStrategy<RuledIndividual> removal() {
		return i -> {
			List<Rule> rules = getRules(i);
			if (rules.size() == 1)
				return i;
			int j = r.nextInt(rules.size() - 1);
			rules.remove(j);
			return i.withNewRules(rules);
		};
	}

	private MutationStrategy<RuledIndividual> addition() {
		return i -> {
			List<Rule> rules = getRules(i);
			Properties prop = Config.INIT;
			int nbOfRangeChecks = Statistics.getChanceIndex(prop
					.getProperty("nbOfRangeChecks")) + 1;
			IntSupplier bounds = () -> Statistics.getChanceIndex(prop
					.getProperty("1vs2bounds")) + 1;
			rules.add(rules.size() - 1,
					RandomSupplier.rule(nbOfRangeChecks, bounds));
			return i.withNewRules(rules);
		};
	}

	public static void main(String[] args) {
		Set<Features> features = DataSet.trainingSets().map(Features::load)
				.collect(Collectors.toSet());
		Evaluator eval = new Evaluator();
		RuledIndividual i = new RandomGenerator(features, eval).get();
		System.out.println(i);
		System.out.println();
		System.out.println(new Mutator().mutate(i));
	}

}
