package genetic;

import genetic.crossover.CrossoverStrategy;
import genetic.mutation.MutationStrategy;
import genetic.selection.SelectionStrategy;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

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
	private Supplier<Population<T>> init;
	private SelectionStrategy<T> selection;
	private MutationStrategy<T> mutation;
	private CrossoverStrategy<T> crossover;

	/*
	 * These fields will change after every iteration of the algorithm itself
	 */
	private Population<T> population;
	private int nbOfIterations;
	private long startTime;
	private double[] progress;

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
		if (init != null)
			return init.get();
		else
			return null; // TODO
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
		selected.forEach(i -> {
			if (r.nextDouble() < mProb)
				mutation.mutate(i);
		});
	}

	public double getMutationProbability() {
		return mProb;
	}

	public double getCrossoverProbability() {
		return coProb;
	}

	public int getPopulationSize() {
		return popSize;
	}

	/**
	 * Returns the number of iterations the algorithm itself has gone through.
	 */
	public int getNbOfIterations() {
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
	 * Gives a view of the progress given by an array in which every entry gives
	 * the fitness of the best individual in that iteration. This means there
	 * are as much entries in this array as there have been iterations.
	 * 
	 * @return
	 */
	public double[] getProgress() {
		return Arrays.copyOf(progress, progress.length);
	}

	private void paperWork() {
		nbOfIterations++;
		updateProgress();
	}

	private void initialize() {
		startTime = System.currentTimeMillis();
		population = initializePopulation();
		nbOfIterations = 0;
		progress = new double[0];
	}

	private void updateProgress() {
		double[] newProgress = Arrays.copyOf(progress, progress.length + 1);
		newProgress[progress.length] = population.bestIndividual().fitness();
		progress = newProgress;
	}

}
