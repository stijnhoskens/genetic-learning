package genetic.crossover;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import genetic.Config;
import genetic.individuals.rules.Rule;
import genetic.individuals.rules.RuledIndividual;
import util.Pair;
import util.Statistics;

public class Crossover implements CrossoverStrategy<RuledIndividual> {

	private final Random r = new Random();

	@Override
	public Pair<RuledIndividual, RuledIndividual> childrenOf(
			Pair<RuledIndividual, RuledIndividual> parents) {
		Properties prop = Config.CROSSOVER;
		int i = Statistics.getChanceIndex(prop.getProperty("typeOfCrossover"));
		return Arrays.asList(condition(), list()).get(i).childrenOf(parents);
	}

	private static List<Rule> getRules(RuledIndividual i) {
		return i.getRules().asList();
	}

	private static void swapRule(List<Rule> rules, int i, Rule newRule) {
		rules.remove(i);
		rules.add(i, newRule);
	}

	private static void swapRules(List<Rule> rules, int i, List<Rule> newRules) {
		rules.addAll(i, newRules);
		rules.subList(i + newRules.size(), rules.size()).clear();
	}

	// TODO interchange conjuncts?

	private CrossoverStrategy<RuledIndividual> condition() {
		return p -> {
			RuledIndividual i1 = p.getFirst();
			List<Rule> rules1 = getRules(i1);
			int index1 = r.nextInt(rules1.size());
			Rule rule1 = rules1.get(index1);
			RuledIndividual i2 = p.getSecond();
			List<Rule> rules2 = getRules(i2);
			int index2 = r.nextInt(rules2.size());
			Rule rule2 = rules2.get(index1);
			Rule newRule1 = rule1.withNewAction(rule2.get());
			Rule newRule2 = rule2.withNewAction(rule1.get());
			swapRule(rules1, index1, newRule1);
			swapRule(rules2, index2, newRule2);
			return new Pair<>(i1.withNewRules(rules1), i2.withNewRules(rules2));
		};
	}

	private CrossoverStrategy<RuledIndividual> list() {
		return p -> {
			RuledIndividual i1 = p.getFirst();
			List<Rule> rules1 = getRules(i1);
			RuledIndividual i2 = p.getSecond();
			List<Rule> rules2 = getRules(i2);
			int index = r.nextInt(Math.min(rules1.size(), rules2.size()));
			List<Rule> sublist1 = new ArrayList<>(rules1.subList(index,
					rules1.size()));
			List<Rule> sublist2 = new ArrayList<>(rules1.subList(index,
					rules1.size()));
			swapRules(rules2, index, sublist1);
			swapRules(rules1, index, sublist2);
			return new Pair<>(i1.withNewRules(rules1), i2.withNewRules(rules2));
		};
	}
}
