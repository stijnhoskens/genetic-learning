package genetic.individuals;

import datasets.stats.DataSetFeatures;

public class DecisionNode implements DTNode {

	private final Range range;
	private final DTNode _if;
	private final DTNode _else;

	public DecisionNode(Range range, DTNode _if, DTNode _else) {
		this.range = range;
		this._if = _if;
		this._else = _else;
	}

	@Override
	public double accuracy(DataSetFeatures data) {
		if (range.contains(data))
			return _if.accuracy(data);
		else
			return _else.accuracy(data);
	}

}
