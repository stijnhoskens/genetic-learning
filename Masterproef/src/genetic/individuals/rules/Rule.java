package genetic.individuals.rules;

import java.nio.file.Path;
import java.util.function.Function;
import java.util.function.Predicate;

import weka.classifiers.Classifier;
import datasets.stats.DataSetFeatures;

public class Rule implements Predicate<DataSetFeatures>,
		Function<Path, Classifier> {

	private final Condition cond;
	private final Action act;

	public Rule(Condition condition, Action action) {
		cond = condition;
		act = action;
	}

	@Override
	public boolean test(DataSetFeatures features) {
		return cond.test(features);
	}

	@Override
	public Classifier apply(Path train) {
		return act.apply(train);
	}

	public static Rule elseRule(Action action) {
		return new Rule(Condition.ELSE, action);
	}

}
