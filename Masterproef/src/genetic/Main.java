package genetic;

import genetic.crossover.Crossover;
import genetic.individuals.Evaluator;
import genetic.individuals.rules.RuledIndividual;
import genetic.init.RandomGenerator;
import genetic.mutation.Mutator;
import genetic.selection.Selection;

import java.util.DoubleSummaryStatistics;
import java.util.Set;
import java.util.stream.Collectors;

import util.LineGraph;
import datasets.DataSet;
import datasets.stats.Features;

public class Main {
	public static void main(String[] args) {
		Set<Features> features = DataSet.trainingSets().map(Features::load)
				.collect(Collectors.toSet());
		Evaluator eval = new Evaluator();
		GeneticAlgorithm<RuledIndividual> genetic = new GeneticAdapter<>(0.1,
				0.6, 500, 0.05);
		genetic.setCrossoverStrategy(new Crossover());
		genetic.setIndividualGenerator(new RandomGenerator(features, eval));
		genetic.setMutationStrategy(new Mutator());
		genetic.setSelectionStrategy(Selection.SUS());
		RuledIndividual best = genetic
				.apply(() -> genetic.getNbOfIterations() >= 150);
		System.out.println(best);
		System.out.println(best.fitness());
		double[] data = genetic.getStatProgress().stream()
				.mapToDouble(DoubleSummaryStatistics::getMax).toArray();
		LineGraph.plot(data);
	}
}
