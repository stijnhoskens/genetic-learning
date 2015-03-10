package genetic.individuals.rules;

import genetic.individuals.RangeCheck;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import util.Joiner;
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
		this.ranges = collapse(ranges);
	}

	private static Collection<RangeCheck> collapse(Collection<RangeCheck> ranges) {
		return ranges
				.stream()
				.collect(
						Collectors.groupingBy(RangeCheck::getIndex, Collectors
								.reducing(RangeCheck.identity(),
										RangeCheck::merge))).values();
	}

	private Condition() {
		ranges = Collections.emptySet();
	}

	public Collection<RangeCheck> getRanges() {
		return Collections.unmodifiableCollection(ranges);
	}

	@Override
	public boolean test(Features features) {
		return ranges.stream().allMatch(r -> r.test(features));
	}

	@Override
	public String toString() {
		return Joiner.join(ranges.stream().map(RangeCheck::toString), " && ");
	}

	public static void main(String[] args) {
		RangeCheck check1 = new RangeCheck(0, 1, Double.POSITIVE_INFINITY);
		RangeCheck check2 = new RangeCheck(0, 0, 2);
		Collection<RangeCheck> checks = Arrays.asList(check1, check2);
		System.out.println(new Condition(checks));
	}

}
