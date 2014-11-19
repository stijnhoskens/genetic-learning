package genetic.individuals;

import java.util.function.Predicate;

import datasets.stats.DataSetFeatures;

public class Range implements Predicate<DataSetFeatures> {

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

	public double getFrom() {
		return from;
	}

	public double getTo() {
		return to;
	}

	@Override
	public boolean test(DataSetFeatures features) {
		double toCheck = features.getEntry(i);
		return toCheck >= from && toCheck < to;
	}

	public Range mutated(double fromInclusive, double toExclusive) {
		return new Range(i, fromInclusive, toExclusive);
	}

}
