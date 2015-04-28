package analysis;

import genetic.individuals.RangeCheck;

public enum TypeOfCheck {

	UPPER_BOUND, LOWER_BOUND, RANGE;

	public static TypeOfCheck of(RangeCheck check) {
		if (check.getFrom() == Double.NEGATIVE_INFINITY
				&& check.getTo() == Double.POSITIVE_INFINITY)
			throw new IllegalStateException("At least one bound must be given");
		if (check.getFrom() == Double.NEGATIVE_INFINITY)
			return UPPER_BOUND;
		if (check.getTo() == Double.POSITIVE_INFINITY)
			return LOWER_BOUND;
		return RANGE;
	}
}
