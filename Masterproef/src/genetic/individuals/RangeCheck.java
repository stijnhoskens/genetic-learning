package genetic.individuals;

import java.util.function.Predicate;

import datasets.stats.Features;

public class RangeCheck implements Predicate<Features> {

	private final int i;
	private final double from;
	private final double to;

	public RangeCheck(int index, double fromInclusive, double toExclusive) {
		i = index;
		from = fromInclusive;
		to = toExclusive;
	}

	/**
	 * Use this method to only initialize one bound. Set isLower to true if that
	 * bound is the lower-bound, set it to false if it's the upper-bound.
	 */
	public RangeCheck(int index, double value, boolean isLower) {
		i = index;
		if (isLower) {
			from = value;
			to = Double.POSITIVE_INFINITY;
		} else {
			from = Double.NEGATIVE_INFINITY;
			to = value;
		}
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
	public boolean test(Features features) {
		double toCheck = features.getEntry(i);
		return toCheck >= from && toCheck < to;
	}

	public RangeCheck mutated(double fromInclusive, double toExclusive) {
		return new RangeCheck(i, fromInclusive, toExclusive);
	}

}
