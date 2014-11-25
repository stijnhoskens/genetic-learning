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

	public static RangeCheck identity() {
		return identity(-1);
	}

	public static RangeCheck identity(int index) {
		return new RangeCheck(index, Double.NEGATIVE_INFINITY,
				Double.POSITIVE_INFINITY);
	}

	/**
	 * @pre both range checks should be for the same index.
	 */
	public RangeCheck merge(RangeCheck other) {
		int index;
		if (i < 0)
			index = other.i;
		else
			index = i;
		double largestFrom = Math.max(from, other.from);
		double smallestTo = Math.min(to, other.to);
		return new RangeCheck(index, largestFrom, smallestTo);
	}

	@Override
	public String toString() {
		return from + " <= features[" + i + "] < " + to;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(from);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + i;
		temp = Double.doubleToLongBits(to);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RangeCheck other = (RangeCheck) obj;
		if (Double.doubleToLongBits(from) != Double
				.doubleToLongBits(other.from))
			return false;
		if (i != other.i)
			return false;
		if (Double.doubleToLongBits(to) != Double.doubleToLongBits(other.to))
			return false;
		return true;
	}

}
