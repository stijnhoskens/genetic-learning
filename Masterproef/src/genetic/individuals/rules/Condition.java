package genetic.individuals.rules;

import genetic.individuals.RangeCheck;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.function.Predicate;

import datasets.stats.Features;

public class Condition implements Predicate<Features> {

	public static final Condition ELSE = new Condition() {
		@Override
		public boolean test(Features features) {
			return true;
		}
	};

	private final Collection<RangeCheck> ranges;

	public Condition(Collection<RangeCheck> ranges) {
		this.ranges = new HashSet<>(ranges);
	}

	private Condition() {
		ranges = Collections.emptySet();
	}

	@Override
	public boolean test(Features features) {
		return ranges.stream().allMatch(r -> r.test(features));
	}

}
