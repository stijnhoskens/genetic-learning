package genetic.individuals;

import datasets.stats.DataSetFeatures;

public class Range {

	private final int i;
	private final double from;
	private final double to;

	public Range(int index, double fromInclusive, double toExclusive) {
		i = index;
		from = fromInclusive;
		to = toExclusive;
	}

	public int getIndex() {
		return i;
	}

	public boolean contains(DataSetFeatures features) {
		double toCheck = features.asArray()[i];
		return toCheck >= from && toCheck < to;
	}

}
