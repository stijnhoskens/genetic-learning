package genetic.individuals.rules;

import genetic.individuals.Evaluator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import datasets.stats.Features;

public class Rule implements Predicate<Features>, Supplier<String> {

	private final Condition cond;
	private final String act;

	private double score = 0;
	private Set<Features> alreadyPassed = new HashSet<>();

	public Rule(Condition condition, String action) {
		cond = condition;
		act = action;
	}

	@Override
	public boolean test(Features features) {
		return cond.test(features);
	}

	@Override
	public String get() {
		return act;
	}

	public double evaluate(Features features, Evaluator evaluator) {
		double evaluation = evaluator.evaluate(get(), features.getDataSet());
		if (!alreadyPassed.contains(features)) {
			double othersScore = score * alreadyPassed.size();
			alreadyPassed.add(features);
			score = (evaluation + othersScore) / alreadyPassed.size();
		}
		return evaluation;
	}

	public double getScore() {
		return score;
	}

	public Set<Features> getApplicableData() {
		return Collections.unmodifiableSet(alreadyPassed);
	}

	public static Rule elseRule(String action) {
		return new Rule(Condition.ELSE, action);
	}

	public Rule withNewCondition(Condition condition) {
		return new Rule(condition, act);
	}

	public Rule withNewAction(String action) {
		return new Rule(cond, action);
	}

	public boolean isUsed() {
		return !alreadyPassed.isEmpty();
	}

	public void clearApplicableData() {
		alreadyPassed.clear();
	}

	@Override
	public String toString() {
		String prefix = cond.equals(Condition.ELSE) ? "ELSE " : "IF "
				+ cond.toString() + " THEN ";
		return prefix
				+ act
				+ " "
				+ getApplicableData().stream().map(Features::getDataSet)
						.collect(Collectors.toSet());
	}
}
