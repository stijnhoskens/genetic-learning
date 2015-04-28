package analysis;

import util.Pair;
import util.Triple;
import genetic.individuals.RangeCheck;

public class FeatClassifierRelation extends Pair<RangeCheck, String> {

	private final String classifier;
	private final RangeCheck check;

	public FeatClassifierRelation(RangeCheck check, String classifier) {
		super(check, classifier);
		this.check = check;
		this.classifier = classifier;
	}

	public int getFeature() {
		return check.getIndex();
	}

	public String getClassifier() {
		return classifier;
	}

	public RangeCheck getCheck() {
		return check;
	}

	public TypeOfCheck typeOfCheck() {
		return TypeOfCheck.of(check);
	}

	public Triple<Integer, String, TypeOfCheck> keyInformation() {
		return new Triple<>(getFeature(), classifier, typeOfCheck());
	}

	@Override
	public String toString() {
		return new StringBuilder().append(getFeature()).append(" - ")
				.append(classifier).append(" (").append(check.toString())
				.append(")").toString();
	}
}
