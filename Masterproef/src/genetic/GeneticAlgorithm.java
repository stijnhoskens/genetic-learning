package genetic;

import genetic.crossover.CrossoverStrategy;
import genetic.init.PopulationGenerator;
import genetic.mutation.MutationStrategy;
import genetic.selection.SelectionStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.BooleanSupplier;

import util.Pair;

public class GeneticAlgorithm<T extends Individual> {

	/*
	 * These fields remain invariant during the full life cycle of an instance
	 * of GeneticAlgorithm
	 */
	private final double mProb;
	private final double coProb;
	private final int popSize;

	/*
	 * Fields used for the actual operators
	 */
	private PopulationGenerator<T> init;
	private SelectionStrategy<T> selection;
	private MutationStrategy<T> mutation;
	private CrossoverStrategy<T> crossover;

	/*
	 * These fields will change after every iteration of the algorithm itself
	 */
	private Population<T> population;
	private int nbOfIterations;
	private long startTime;
	private final List<Individual> progress = new ArrayList<>();

	private final Random r = new Random();

	public GeneticAlgorithm(double mutationP, double crossoverP,
			int populationSize) {
		mProb = mutationP;
		coProb = crossoverP;
		popSize = populationSize;
	}

	public Individual apply(BooleanSupplier termination) {
		initialize();
		while (!termination.getAsBoolean()) {
			List<T> selected = select();
			crossover(selected);
			mutate(selected);
			population.replaceWith(selected);
			paperWork();
		}
		return population.bestIndividual();
	}

	protected Population<T> initializePopulation() {
		return init.get(popSize);
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
		return selection.selectionOf(population);
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
		for (int i = 0; i < selected.size() / 2; i++)
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

	public void setPopulationGenerator(PopulationGenerator<T> supplier) {
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
	public synchronized List<Individual> getProgress() {
		return Collections.unmodifiableList(progress);
	}

	private synchronized void paperWork() {
		nbOfIterations++;
		updateProgress();
	}

	private void initialize() {
		startTime = System.currentTimeMillis();
		population = initializePopulation();
		nbOfIterations = 0;
	}

	private void updateProgress() {
		progress.add(population.bestIndividual());
	}

}
