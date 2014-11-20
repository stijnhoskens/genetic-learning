package genetic.individuals.tree;

import genetic.individuals.RangeCheck;
import datasets.stats.Features;

public class DecisionNode implements DTNode {

	private final RangeCheck range;
	private final DTNode _if;
	private final DTNode _else;

	public DecisionNode(RangeCheck range, DTNode _if, DTNode _else) {
		this.range = range;
		this._if = _if;
		this._else = _else;
	}

	@Override
	public double accuracy(Features data) {
		if (range.test(data))
			return _if.accuracy(data);
		else
			return _else.accuracy(data);
	}

}
