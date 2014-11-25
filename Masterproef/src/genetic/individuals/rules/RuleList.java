package genetic.individuals.rules;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import util.Joiner;
import datasets.stats.Features;

public class RuleList implements Function<Features, String> {

	private final List<Rule> rules;
	private final Rule _else;

	public RuleList(List<Rule> rules, String _else) {
		this.rules = new ArrayList<>(rules);
		this._else = Rule.elseRule(_else);
	}

	/**
	 * @note this constructor ALWAYS takes the last rule in the list as the
	 *       else-rule
	 * @throws IllegalArgumentException
	 *             if the rule list size == 0
	 */
	public RuleList(List<Rule> rules) throws IllegalArgumentException {
		if (rules.size() == 0)
			throw new IllegalArgumentException(
					"The size of the rule list can't be zero");
		_else = Rule.elseRule(rules.get(rules.size() - 1).get());
		this.rules = new ArrayList<>(rules);
		this.rules.remove(this.rules.size() - 1);
	}

	@Override
	public String apply(Features features) {
		for (Rule rule : rules)
			if (rule.test(features))
				return rule.get();
		return _else.get();
	}

	public List<Rule> asList() {
		List<Rule> r = new ArrayList<>(rules);
		r.add(_else);
		return r;
	}

	@Override
	public String toString() {
		if (rules.size() == 0)
			return _else.get();
		return Joiner.join(rules.stream().map(Rule::toString), "\n")
				+ "\nELSE " + _else.get();
	}
}
