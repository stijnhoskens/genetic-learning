package genetic.individuals.rules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import util.Joiner;
import datasets.stats.Features;

public class RuleList implements Function<Features, Rule> {

	private final List<Rule> rules;
	private Rule _else;

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
	public synchronized Rule apply(Features features) {
		for (Rule r : rules)
			if (r.test(features))
				return r;
		return _else;
	}

	void removeUnusedRulesAndDuplicates() {
		List<Rule> copy = new ArrayList<>(rules);
		rules.clear();
		copy.stream().filter(Rule::isUsed).forEach(r -> {
			if (!rules.contains(r))
				rules.add(r);
		});
		if (!_else.isUsed()) {
			int lastIndex = rules.size() - 1;
			_else = rules.get(lastIndex);
			rules.remove(lastIndex);
		}
	}

	public List<Rule> asList() {
		List<Rule> r = new ArrayList<>(rules);
		r.add(_else);
		return Collections.unmodifiableList(r);
	}

	@Override
	public String toString() {
		if (rules.size() == 0)
			return _else.get();
		return Joiner.join(
				Stream.concat(rules.stream(), Stream.of(_else)).map(
						Rule::toString), "\n");
	}

	public void resetApplicableData() {
		rules.forEach(Rule::clearApplicableData);
		_else.clearApplicableData();
	}
}
