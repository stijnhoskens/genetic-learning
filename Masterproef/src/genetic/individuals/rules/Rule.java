package genetic.individuals.rules;

import genetic.individuals.Evaluator;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

import datasets.stats.Features;

public class Rule implements Predicate<Features>, Supplier<String> {

	private final Condition cond;
	private final String act;

	private double score = 0;
	private Set<Features> alreadyPassed = new HashSet<>();
	private boolean isUsed = false;

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
		isUsed  = true;
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
		return isUsed;
	}

	@Override
	public String toString() {
		return "IF " + cond.toString() + " THEN " + act;
	}

}
