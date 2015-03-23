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

import datasets.DataSet;
import datasets.stats.Features;

public class Analyzer {

	public static void main(String[] args) {
		GeneticAlgorithm<RuledIndividual> ga = defaultSettings();
		MultipleRunExtractor extractor = new MultipleRunExtractor(ga);
		extractor.run(10);
		System.out.println(extractor.classifierBag());
		System.out.println(extractor.featureBag());
		System.out.println(extractor.nbOfRulesBag());
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
