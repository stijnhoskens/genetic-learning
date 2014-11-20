package genetic.individuals.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import datasets.stats.Features;

public class RuleList implements Function<Features, String> {

	private final List<Rule> rules;
	private final Rule _else;

	public RuleList(List<Rule> rules, Action _else) {
		this.rules = new ArrayList<>(rules);
		this._else = Rule.elseRule(_else);
	}

	@Override
	public String apply(Features features) {
		for (Rule rule : rules)
			if (rule.test(features))
				return rule.get();
		return _else.get();
	}

	public List<Rule> getRules() {
		List<Rule> r = new ArrayList<>(rules);
		r.add(_else);
		return Collections.unmodifiableList(r);
	}
}
