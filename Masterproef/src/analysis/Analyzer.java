package analysis;

import genetic.GeneticAdapter;
import genetic.GeneticAlgorithm;
import genetic.GeneticConfiguration;
import genetic.crossover.Crossover;
import genetic.individuals.Evaluator;
import genetic.individuals.rules.RuledIndividual;
import genetic.init.RandomGenerator;
import genetic.mutation.Mutator;
import genetic.selection.Selection;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import datasets.DataSet;
import datasets.stats.Features;

public class Analyzer {

	public static void main(String[] args) {
		GeneticAlgorithm<RuledIndividual> ga = defaultSettings();
		Extractor extractor = new Extractor(ga);
		BagStatistics<String> classifiers = new BagStatistics<>();
		BagStatistics<Integer> features = new BagStatistics<>(), nbOfRules = new BagStatistics<>();
		IntStream.range(0, 200).forEach(i -> {
			ga.apply();
			classifiers.accept(extractor.classifiersDuringRun());
			features.accept(extractor.featuresDuringRun());
			nbOfRules.accept(extractor.nbOfRulesDuringRun());
		});
		System.out.println(classifiers);
		System.out.println(features);
		System.out.println(nbOfRules);
	}

	private static GeneticAlgorithm<RuledIndividual> defaultSettings() {
		Set<Features> train = DataSet.trainingSets().map(Features::load)
				.collect(Collectors.toSet());
		GeneticConfiguration config = new GeneticConfiguration(0.2, 0.5, 200,
				0.05);
		GeneticAlgorithm<RuledIndividual> algorithm = new GeneticAdapter<>(
				config);
		Evaluator eval = new Evaluator();
		algorithm.setCrossoverStrategy(new Crossover());
		algorithm.setIndividualGenerator(new RandomGenerator(train, eval));
		algorithm.setMutationStrategy(new Mutator());
		algorithm.setSelectionStrategy(Selection.SUS());
		algorithm.setTerminationCriterium(() -> {
			return algorithm.getNbOfIterations() >= 300;
		});
		return algorithm;
	}
}
