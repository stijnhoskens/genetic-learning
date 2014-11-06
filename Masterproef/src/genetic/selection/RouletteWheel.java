package genetic.selection;

import genetic.Individual;
import genetic.Population;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.ToDoubleFunction;

public class RouletteWheel<T extends Individual> {

	private final List<Entry<T>> entries = new ArrayList<>();
	private final Random r = new Random();

	public RouletteWheel(Population<T> population, ToDoubleFunction<T> ranker) {
		double accumulator = 0;
		for (T i : population.asList()) {
			accumulator += ranker.applyAsDouble(i);
			entries.add(new Entry<>(i, accumulator));
		}
		final double totalSum = accumulator;
		entries.forEach(e -> e.normalize(totalSum));
	}

	public RouletteWheel(Population<T> population) {
		this(population, Individual::fitness);
	}

	public T spin() {
		double random = r.nextDouble();
		for (Entry<T> e : entries)
			if (e.accumulatedValueIsGreaterThan(random))
				return e.getIndividual();
		return null;
	}

	public List<T> universalSpin(int n) {
		List<T> individuals = new ArrayList<>();
		double spacing = 1d / (double) n;
		double random = r.nextDouble() * spacing;
		for (Entry<T> e : entries)
			while (e.accumulatedValueIsGreaterThan(random)) {
				individuals.add(e.getIndividual());
				random += spacing;
			}
		return individuals;
	}

	private static class Entry<T> {
		private final T ind;
		private double accumVal;

		public Entry(T individual, double accumulatedValue) {
			ind = individual;
			accumVal = accumulatedValue;
		}

		public void normalize(double totalSum) {
			accumVal /= totalSum;
		}

		public boolean accumulatedValueIsGreaterThan(double value) {
			return accumVal > value;
		}

		public T getIndividual() {
			return ind;
		}
	}

}
