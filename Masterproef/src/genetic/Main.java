package genetic;

import genetic.crossover.Crossover;
import genetic.individuals.Evaluator;
import genetic.individuals.rules.RuledIndividual;
import genetic.init.RandomGenerator;
import genetic.mutation.Mutator;
import genetic.selection.Selection;

import java.util.Comparator;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import learning.Classifiers;
import util.Pair;
import datasets.DataSet;
import datasets.stats.Features;

public class Main {

	public static void main(String[] args) {
		testGeneticAlgorithm();
	}

	private static Set<Features> featuresOf(Stream<DataSet> data) {
		return data.map(Features::load).collect(Collectors.toSet());
	}

	public static void randomTestTrainSplit() {
		for (;;) {
			final Random r = new Random();
			Set<DataSet> trainingsets = DataSet.all()
					.filter(ds -> r.nextBoolean()).collect(Collectors.toSet());
			Set<Features> train = featuresOf(trainingsets.stream());
			Set<Features> test = featuresOf(DataSet.all().filter(
					ds -> !trainingsets.contains(ds)));
			System.out.println("Best solution for test = "
					+ findBestSolution(test));
			testGeneticAlgorithm(train, test);
		}
	}

	public static void testGeneticAlgorithm() {
		testGeneticAlgorithm(featuresOf(DataSet.trainingSets()),
				featuresOf(DataSet.testSets()));
	}

	public static void testGeneticAlgorithm(Set<Features> train,
			Set<Features> test) {
		Evaluator eval = new Evaluator();
		GeneticAlgorithm<RuledIndividual> genetic = new GeneticAdapter<>(0.2,
				0.5, 200, 0.05);
		genetic.setCrossoverStrategy(new Crossover());
		genetic.setIndividualGenerator(new RandomGenerator(train, eval));
		genetic.setMutationStrategy(new Mutator());
		genetic.setSelectionStrategy(Selection.SUS());
		RuledIndividual best = genetic
				.apply(() -> genetic.getNbOfIterations() >= 300);
		System.out.println(best);
		// double[] data = genetic.getStatProgress().stream()
		// .mapToDouble(DoubleSummaryStatistics::getMax).toArray();
		best.setData(test);
		System.out.println("Testing fitness: " + best.fitness());
		System.out.println(best);
		// LineGraph.plot(data);
	}

	public static double findBestSolution(Set<Features> data) {
		Evaluator eval = new Evaluator();
		double accum = 0;
		for (Features f : data) {
			DataSet ds = f.getDataSet();
			Pair<String, Double> result = Classifiers.allOptions()
					.map(s -> new Pair<>(s, eval.evaluate(s, ds)))
					.max(Comparator.comparingDouble(Pair::getSecond)).get();
			System.out.println(result.getFirst() + " @ " + ds.toString()
					+ ", score=" + result.getSecond() + ".");
			accum += result.getSecond();
		}
		return accum / (double) data.size();
	}
}
