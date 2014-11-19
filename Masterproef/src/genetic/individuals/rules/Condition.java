package genetic.individuals.rules;

import genetic.individuals.Range;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.function.Predicate;

import datasets.stats.DataSetFeatures;

public class Condition implements Predicate<DataSetFeatures> {

	public static final Condition ELSE = new Condition() {
		@Override
		public boolean test(DataSetFeatures features) {
			return true;
		}
	};

	private final Collection<Range> ranges;

	public Condition(Collection<Range> ranges) {
		this.ranges = new HashSet<>(ranges);
	}

	private Condition() {
		ranges = Collections.emptySet();
	}

	@Override
	public boolean test(DataSetFeatures features) {
		return ranges.stream().allMatch(r -> r.test(features));
	}

}
