package genetic.individuals.rules;

import genetic.individuals.Evaluator;
import genetic.individuals.Individual;

import java.util.List;
import java.util.Set;

import datasets.stats.Features;

public class RuledIndividual implements Individual {

	private double fitness;
	private Set<Features> data;
	private final RuleList rList;
	private final Evaluator eval;

	public RuledIndividual(Set<Features> datasets, RuleList rules,
			Evaluator evaluator) {
		data = datasets;
		rList = rules;
		eval = evaluator;
		fitness = -1;
	}

	@Override
	public double fitness() {
		if (fitness < 0)
			fitness = calculateFitness();
		return fitness;
	}

	public void setData(Set<Features> data) {
		fitness = -1;
		this.data = data;
	}

	private double calculateFitness() {
		rList.resetApplicableData();
		double evaluation = data.stream()
				.mapToDouble(f -> rList.apply(f).evaluate(f, eval)).average()
				.orElse(0);
		rList.removeUnusedRulesAndDuplicates();
		// long count = rList.asList().stream()
		// .flatMap(r -> r.getApplicableData().stream()).count();
		// if (count > 9)
		//	System.out.println("Bug detected!");
		return evaluation;
	}

	public Evaluator getEvaluator() {
		return eval;
	}

	public RuleList getRules() {
		return rList;
	}

	public RuledIndividual withNewRules(RuleList rules) {
		return new RuledIndividual(data, rules, eval);
	}

	public RuledIndividual withNewRules(List<Rule> rules) {
		return withNewRules(new RuleList(rules));
	}

	@Override
	public String toString() {
		String fit = fitness == -1 ? "not yet calculated" : String
				.valueOf(fitness);
		return rList.toString() + "\nfitness: " + fit;
	}
}
