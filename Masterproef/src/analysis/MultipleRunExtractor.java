package analysis;

import java.util.stream.IntStream;

import util.Bag;
import genetic.GeneticAlgorithm;
import genetic.individuals.rules.RuledIndividual;

public class MultipleRunExtractor {

	private final Extractor extractor;
	private final GeneticAlgorithm<RuledIndividual> ga;

	public MultipleRunExtractor(GeneticAlgorithm<RuledIndividual> ga) {
		this.ga = ga;
		extractor = new Extractor(ga);
	}

	public void run(int nbOfRuns) {
		resetResults();
		IntStream.range(0, nbOfRuns).forEach(x -> {
			ga.apply();
			acceptNewResult();
		});
	}

	private void resetResults() {
		features.clear();
		classifiers.clear();
	}

	private void acceptNewResult() {
		features.addAll(extractor.featuresDuringRun());
		classifiers.addAll(extractor.classifiersDuringRun());
	}

	private final Bag<Integer> features = new Bag<>();

	public Bag<Integer> featureBag() {
		return features.unmodifiable();
	}

	private final Bag<String> classifiers = new Bag<>();

	public Bag<String> classifierBag() {
		return classifiers.unmodifiable();
	}

}
