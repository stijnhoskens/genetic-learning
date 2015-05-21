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
import genetic.individuals.rules.RuleList;
import genetic.individuals.rules.RuledIndividual;
import genetic.init.RandomGenerator;
import genetic.mutation.Mutator;
import genetic.selection.Selection;
import io.IO;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import util.Bag;
import util.LineGraph;
import util.Pair;
import util.Triple;
import datasets.DataSet;
import datasets.stats.DoubleDistribution;
import datasets.stats.Features;

public class Analyzer {

	private final static AtomicInteger counter = new AtomicInteger(0);

	public static void main(String[] args) {
		analyzeIndexClassifierCorrelation2();
	}

	public static void analyzeIndexClassifierCorrelation2() {
		Set<FeatClassifierRelation> relations = Collections
				.synchronizedSet(new HashSet<>());
		int n = 2000;
		IntStream
				.range(0, n)
				.parallel()
				.forEach(
						i -> {
							System.out.println(counter.incrementAndGet());
							GeneticAlgorithm<RuledIndividual> ga = defaultSettings();
							ga.apply();
							Set<RuledIndividual> best = new HashSet<>(ga
									.getProgress());
							best.stream()
									.flatMap(
											ri -> ri.getRules().asList()
													.stream())
									.forEach(
											r -> r.getCondition()
													.getRanges()
													.forEach(
															c -> relations
																	.add(new FeatClassifierRelation(
																			c,
																			r.get()))));
						});

		Map<Triple<Integer, String, TypeOfCheck>, List<FeatClassifierRelation>> mapped = relations
				.stream()
				.collect(
						Collectors
								.groupingBy(FeatClassifierRelation::keyInformation));
		List<ReducedRelation> reduced = mapped
				.entrySet()
				.stream()
				.map(e -> {
					Triple<Integer, String, TypeOfCheck> triple = e.getKey();
					Collection<RangeCheck> checks = e.getValue().stream()
							.map(FeatClassifierRelation::getCheck)
							.collect(Collectors.toSet());
					return new ReducedRelation(triple.getFirst(), triple
							.getSecond(), triple.getThird(), checks);
				}).sorted(ReducedRelation.comparator())
				.collect(Collectors.toList());
		reduced.forEach(r -> System.out.println(r.getCheck() + " => "
				+ r.getClassifier() + " (" + r.getCount() + ")"));
	}

	public static void analyzeIndexClassifierCorrelation() {
		Bag<Pair<Set<Integer>, String>> corr = Bag.parallel();
		int n = 500;
		LocalTime start = LocalTime.now();
		IntStream
				.range(0, n)
				.parallel()
				.forEach(
						i -> {
							GeneticAlgorithm<RuledIndividual> ga = defaultSettings();
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
							best.forEach(ri -> {
								List<Rule> rules = ri.getRules().asList();
								if (rules.size() <= 1)
									return;
								Rule last = rules.get(rules.size() - 1);
								Rule beforeLast = rules.get(rules.size() - 2);
								corr.add(new Pair<Set<Integer>, String>(
										beforeLast.getCondition()
												.getCheckedIndices(), last
												.get()));
							});
						});
		System.out.println("Elapsed time: "
				+ Duration.between(start, LocalTime.now()).getSeconds() + "s.");
		Bag<Pair<Integer, String>> result = new Bag<>();
		corr.stream().forEach(
				p -> p.getFirst().forEach(
						i -> result.add(new Pair<>(i, p.getSecond()))));
		Comparator<Pair<Integer, String>> comp = (p0, p1) -> p0.getSecond()
				.compareTo(p1.getSecond());
		comp = comp.thenComparing((p0, p1) -> Integer.compare(result.count(p1),
				result.count(p0)));
		System.out.println(result.toString(comp));
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

	public static void analyzeEvolution() {
		List<Population<RuledIndividual>> evo = evolution().collect(
				Collectors.toList());
		analyzeEvolution(evo, DoubleDistribution::average, "averages");
		analyzeEvolution(evo, dd -> dd.frequencyStats().getMin(), "minima");
		analyzeEvolution(evo, dd -> dd.frequencyStats().getMax(), "maxima");
		analyzeEvolution(evo, DoubleDistribution::standardDeviation, "stds");
		plotStds();
	}

	private static void analyzeEvolution(List<Population<RuledIndividual>> evo,
			ToDoubleFunction<DoubleDistribution> f, String name) {
		Stream<Double> data = evo.stream().map(Population::fitness)
				.mapToDouble(f).boxed();
		IO.write(
				Paths.get("matlab/plot/" + name + ".txt"),
				w -> {
					try {
						w.write(data.map(String::valueOf).collect(
								Collectors.joining("\n")));
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
	}

	private static void plotStds() {
		Path path = Paths.get("matlab/plot/stds.txt");
		LineGraph.plot("Std of the population during a run", IO.lines(path)
				.mapToDouble(Double::valueOf).toArray());
	}

	public static void analyzeDiversity() {
		LineGraph.plot("Diversity measure during one run", evolution()
				.mapToDouble(DiversityMeasure::of).toArray());
	}

	public static void analyzeSizeEvolution() {
		LineGraph.plot(
				"Avg nb of rules during one run",
				evolution().mapToDouble(
						p -> p.asStream().map(RuledIndividual::getRules)
								.map(RuleList::asList).mapToInt(List::size)
								.average().orElse(0)).toArray());
	}

	private static void consumeEvolution(
			Consumer<Population<RuledIndividual>> cons) {
		GeneticAlgorithm<RuledIndividual> ga = defaultSettings();
		ga.setPopulationConsumer(cons);
		ga.apply();
	}

	private static Stream<Population<RuledIndividual>> evolution() {
		Builder<Population<RuledIndividual>> builder = Stream.builder();
		consumeEvolution(p -> builder.accept(p));
		return builder.build();
	}

	private final static int NB_OF_ITERATIONS = 500;

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
			return algorithm.getNbOfIterations() >= NB_OF_ITERATIONS;
		});
		return algorithm;
	}
}
