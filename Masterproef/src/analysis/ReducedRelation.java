package analysis;

import genetic.individuals.RangeCheck;

import java.util.Collection;
import java.util.Comparator;

public class ReducedRelation extends FeatClassifierRelation {

	private final int feature;
	private RangeCheck reduced;
	private int count;

	public ReducedRelation(int feature, String classifier, TypeOfCheck type,
			Collection<RangeCheck> checks) {
		super(null, classifier);
		this.feature = feature;
		this.count = checks.size();
		reduce(type, checks);
	}

	@Override
	public int getFeature() {
		return feature;
	}

	@Override
	public RangeCheck getCheck() {
		return reduced;
	}

	public int getCount() {
		return count;
	}

	private void reduce(TypeOfCheck type, Collection<RangeCheck> relations) {
		switch (type) {
		case LOWER_BOUND:
			double avg = relations.stream().mapToDouble(RangeCheck::getFrom)
					.average().orElse(Double.NEGATIVE_INFINITY);
			reduced = new RangeCheck(feature, avg, true);
			break;
		case UPPER_BOUND:
			avg = relations.stream().mapToDouble(RangeCheck::getTo).average()
					.orElse(Double.POSITIVE_INFINITY);
			reduced = new RangeCheck(feature, avg, false);
			break;
		case RANGE:
			avg = relations.stream().mapToDouble(RangeCheck::getFrom).average()
					.orElse(Double.NEGATIVE_INFINITY);
			double avg2 = relations.stream().mapToDouble(RangeCheck::getTo)
					.average().orElse(Double.POSITIVE_INFINITY);
			reduced = new RangeCheck(feature, avg, avg2);
		default:
			break;
		}

	}

	public static Comparator<ReducedRelation> comparator() {
		return Comparator.comparingInt(ReducedRelation::getCount).reversed();
	}
}
