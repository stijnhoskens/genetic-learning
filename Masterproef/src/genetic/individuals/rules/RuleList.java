package genetic.individuals.rules;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import weka.classifiers.Classifier;
import datasets.stats.DataSetFeatures;

public class RuleList implements Function<DataSetFeatures, Classifier> {

	private final List<Rule> rules;
	private final Rule _else;

	public RuleList(List<Rule> rules, Rule _else) {
		this.rules = new ArrayList<>(rules);
		this._else = _else;
	}

	public RuleList(List<Rule> rules, Action _else) {
		this.rules = new ArrayList<>(rules);
		this._else = Rule.elseRule(_else);
	}

	@Override
	public Classifier apply(DataSetFeatures features) {
		Path train = features.getDataSet().train();
		for (Rule rule : rules)
			if (rule.test(features))
				return rule.apply(train);
		return _else.apply(train);
	}
}
