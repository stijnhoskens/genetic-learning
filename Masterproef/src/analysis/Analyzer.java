package analysis;

import genetic.GeneticAdapter;
import genetic.GeneticAlgorithm;
import genetic.GeneticConfiguration;
import genetic.Population;
import genetic.crossover.Crossover;
import genetic.individuals.Evaluator;
import genetic.individuals.RangeCheck;
import genetic.individuals.rules.Condition;
import genetic.individuals.rules.Rule;
import genetic.individuals.rules.RuledIndividual;
import genetic.init.RandomGenerator;
import genetic.mutation.Mutator;
import genetic.selection.Selection;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import util.Bag;
import util.Pair;
import datasets.DataSet;
import datasets.stats.Features;

public class Analyzer {

	public static void main(String[] args) {
		analyzeDiversity();
	}

	public static void analyzeIndexClassifierCorrelation() {
		GeneticAlgorithm<RuledIndividual> ga = defaultSettings();
		Bag<Pair<Set<Integer>, String>> corr = new Bag<>();
		int n = 100;
		IntStream
				.range(0, n)
				.forEach(
						i -> {
							if (i % 50 == 0)
								System.out.println((i * 100) / n
										+ "% complete.");
							ga.apply();
							Set<RuledIndividual> best = new HashSet<>(ga
									.getProgress());
							best.forEach(ri -> ri
									.getRules()
									.asList()
									.forEach(
											r -> corr
													.add(new Pair<Set<Integer>, String>(
															r.getCondition()
																	.getCheckedIndices(),
															r.get()))));
						});
		System.out.println(corr);
	}

	public static void analyzeFinalSolutions() {
		GeneticAlgorithm<RuledIndividual> ga = defaultSettings();
		Bag<String> classifiers = new Bag<>();
		Bag<Integer> features = new Bag<>(), nbOfRules = new Bag<>();
		int n = 500;
		IntStream.range(0, n).forEach(
				i -> {
					if (i % 50 == 0)
						System.out.println((i * 100) / n + "% complete.");
					RuledIndividual best = ga.apply();
					best.getRules().asList().stream().map(Rule::get)
							.forEach(classifiers::add);
					best.getRules().asList().stream().map(Rule::getCondition)
							.map(Condition::getRanges)
							.flatMap(Collection::stream)
							.mapToInt(RangeCheck::getIndex).boxed()
							.forEach(features::add);
					nbOfRules.add(best.getRules().asList().size());
				});
		System.out.println(classifiers);
		System.out.println(features);
		System.out.println(nbOfRules);
	}

	public static void analyzeAllBestSolutions() {
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

	public static void analyzeDiversity() {
		consumeEvolution(p -> {
			System.out.println(DiversityMeasure.of(p));
		});
	}

	private static void consumeEvolution(
			Consumer<Population<RuledIndividual>> cons) {
		GeneticAlgorithm<RuledIndividual> ga = defaultSettings();
		ga.setPopulationConsumer(cons);
		ga.apply();
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
