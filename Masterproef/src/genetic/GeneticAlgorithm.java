package genetic;

import genetic.crossover.CrossoverStrategy;
import genetic.individuals.Individual;
import genetic.init.IndividualGenerator;
import genetic.mutation.MutationStrategy;
import genetic.selection.SelectionStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.BooleanSupplier;
import java.util.stream.Stream;

import util.Pair;

public abstract class GeneticAlgorithm<T extends Individual> {

	/*
	 * These fields remain invariant during the full life cycle of an instance
	 * of GeneticAlgorithm
	 */
	private final double mProb;
	private final double coProb;
	private final int popSize;
	private final double elitist;

	/*
	 * Fields used for the actual operators
	 */
	private IndividualGenerator<T> init;
	private SelectionStrategy<T> selection;
	private MutationStrategy<T> mutation;
	private CrossoverStrategy<T> crossover;

	/*
	 * Termination criterium
	 */
	private BooleanSupplier termination;

	/*
	 * These fields will change after every iteration of the algorithm itself
	 */
	protected Population<T> population;
	private int nbOfIterations;
	private long startTime;
	private final List<T> progress = new ArrayList<>();
	private final List<DoubleSummaryStatistics> statProgress = new ArrayList<>();

	private final Random r = new Random();
	private Runnable afterEachRun;

	public GeneticAlgorithm(double mutationP, double crossoverP,
			int populationSize, double elite) {
		mProb = mutationP;
		coProb = crossoverP;
		popSize = populationSize;
		elitist = elite;
	}

	public GeneticAlgorithm(GeneticConfiguration config) {
		this(config.mProb, config.coProb, config.popSize, config.elitist);
	}

	public T apply(BooleanSupplier termination) {
		initialize();
		while (!termination.getAsBoolean()) {
			List<T> selected = select();
			List<T> elites = elites();
			crossover(selected);
			mutate(selected);
			selected.addAll(elites);
			population.replaceWith(selected);
			paperWork();
			problemSpecific();
			if (afterEachRun != null)
				afterEachRun.run();
		}
		return population.bestIndividual();
	}

	public T apply() {
		if (termination == null)
			throw new IllegalStateException("Termination criterium not set. "
					+ "Specify one or use the apply(BooleanSupplier) method.");
		return apply(termination);
	}

	protected Population<T> initializePopulation() {
		return new Population<>(init, popSize);
	}

	/**
	 * Selects the individuals from the existing population most fit for further
	 * reproduction. This returns a list with size popSize but most probably not
	 * the same as the initial population. Copies of individuals exist.
	 * 
	 * @return a list (containing a subset of the population) of individuals
	 *         with size popSize
	 */
	protected List<T> select() {
		return selection.selectionOf(population, elitist);
	}

	/**
	 * Crosses over the list of individuals previously selected so fitter
	 * children appear. This crossover will occur with a chance of coProb. This
	 * basically means a coProb of 0% returns an exact replica of the parents,
	 * while a coProb of 100% returns an all new set of children.
	 * 
	 * @note for correctness it is preferred to have an even population size.
	 */
	protected void crossover(List<T> selected) {
		for (int i = 0; i < selected.size() / 2 - 1; i++)
			if (r.nextDouble() < coProb) {
				int j = 2 * i;
				int k = 2 * i + 1;
				Pair<T, T> pair = crossover.childrenOf(new Pair<>(selected
						.get(j), selected.get(k)));
				selected.remove(j);
				selected.remove(k);
				selected.addAll(j, Pair.asList(pair));
			}
	}

	/**
	 * Mutates some individuals in the list (with a chance of mProb).
	 */
	protected void mutate(List<T> selected) {
		for (int i = 0; i < selected.size(); i++)
			if (r.nextDouble() < mProb) {
				T individual = selected.get(i);
				T mutated = mutation.mutate(individual);
				selected.remove(i);
				selected.add(i, mutated);
			}
	}

	protected abstract void problemSpecificInitialisation();

	protected abstract void problemSpecific();

	public void afterEachRun(Runnable doThis) {
		afterEachRun = doThis;
	}

	public void setIndividualGenerator(IndividualGenerator<T> supplier) {
		init = supplier;
	}

	public void setSelectionStrategy(SelectionStrategy<T> strategy) {
		selection = strategy;
	}

	public void setMutationStrategy(MutationStrategy<T> strategy) {
		mutation = strategy;
	}

	public void setCrossoverStrategy(CrossoverStrategy<T> strategy) {
		crossover = strategy;
	}

	public void setTerminationCriterium(BooleanSupplier supplier) {
		termination = supplier;
	}

	/**
	 * Returns the number of iterations the algorithm itself has gone through.
	 */
	public synchronized int getNbOfIterations() {
		return nbOfIterations;
	}

	/**
	 * Returns the elapsed time between the start of the genetic algorithm and
	 * the current time.
	 */
	public long getElapsedTime() {
		return System.currentTimeMillis() - startTime;
	}

	/**
	 * Gives a view of the progress given by a list in which every entry gives
	 * the fittest individual in that iteration. This means there are as much
	 * entries in this list as there have been iterations.
	 */
	public synchronized List<T> getProgress() {
		return Collections.unmodifiableList(progress);
	}

	/**
	 * Returns the best so far individual. ( == the last element in
	 * getProgress).
	 */
	public synchronized T bestSoFar() {
		return progress.get(progress.size() - 1);
	}

	public synchronized List<DoubleSummaryStatistics> getStatProgress() {
		return Collections.unmodifiableList(statProgress);
	}

	private List<T> elites() {
		return new ArrayList<>(population.asSortedList().subList(0,
				(int) (population.size() * elitist)));
	}

	private synchronized void paperWork() {
		nbOfIterations++;
		updateProgress();
	}

	private void initialize() throws RuntimeException {
		problemSpecificInitialisation();
		startTime = System.currentTimeMillis();
		population = initializePopulation();
		nbOfIterations = 0;
		progress.clear();
		statProgress.clear();
		if (Stream.of(init, selection, crossover, mutation).anyMatch(
				Objects::isNull))
			throw new IllegalStateException(
					"One or more of the genetic operator is not set.");
	}

	private void updateProgress() {
		progress.add(population.bestIndividual());
		statProgress.add(population.asList().stream().mapToDouble(T::fitness)
				.summaryStatistics());
	}

}
