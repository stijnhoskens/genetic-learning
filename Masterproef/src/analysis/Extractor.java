package analysis;

import genetic.GeneticAlgorithm;
import genetic.individuals.RangeCheck;
import genetic.individuals.rules.Condition;
import genetic.individuals.rules.Rule;
import genetic.individuals.rules.RuleList;
import genetic.individuals.rules.RuledIndividual;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import util.Bag;

public class Extractor {

	private final GeneticAlgorithm<RuledIndividual> ga;

	/**
	 * @param ga
	 *            The genetic algorithm from which you wish to extract data of a
	 *            run. Run the algorithm (apply) before doing any extraction, as
	 *            the extractor will not do this.
	 */
	public Extractor(GeneticAlgorithm<RuledIndividual> ga) {
		this.ga = ga;
	}

	public Set<RuledIndividual> bestDuringRun() {
		return new HashSet<>(ga.getProgress());
	}

	private Stream<Rule> rulesDuringRun() {
		return bestDuringRun().stream().flatMap(
				i -> i.getRules().asList().stream());
	}

	public Bag<Integer> featuresDuringRun() {
		return rulesDuringRun().map(Rule::getCondition)
				.map(Condition::getRanges).flatMap(Collection::stream)
				.mapToInt(RangeCheck::getIndex).boxed()
				.collect(Bag.collector());
	}

	public Bag<String> classifiersDuringRun() {
		return rulesDuringRun().map(Rule::get).collect(Bag.collector());
	}

	public Bag<Integer> nbOfRulesDuringRun() {
		return bestDuringRun().stream().map(RuledIndividual::getRules)
				.map(RuleList::asList).map(List::size).collect(Bag.collector());
	}
}
