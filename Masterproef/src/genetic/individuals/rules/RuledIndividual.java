package genetic.individuals.rules;

import genetic.individuals.Evaluator;
import genetic.individuals.Individual;

import java.util.Set;

import datasets.stats.Features;

public class RuledIndividual implements Individual {

	private double fitness = -1;
	private final Set<Features> data;
	private final RuleList rList;
	private final Evaluator eval;

	public RuledIndividual(Set<Features> datasets, RuleList rules,
			Evaluator evaluator) {
		data = datasets;
		rList = rules;
		eval = evaluator;
	}

	@Override
	public double fitness() {
		if (fitness < 0)
			fitness = calculateFitness();
		return fitness;
	}

	private double calculateFitness() {
		return eval.evaluate(rList, data);
	}

	public Evaluator getEvaluator() {
		return eval;
	}
}
