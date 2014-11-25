package genetic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import util.Joiner;
import exceptions.PopulationSizeException;
import genetic.individuals.Individual;
import genetic.init.IndividualGenerator;

public class Population<T extends Individual> {

	private final List<T> individuals;

	public Population(Collection<T> individuals) {
		this.individuals = new ArrayList<>(individuals);
	}

	public Population(IndividualGenerator<T> generator, int size) {
		individuals = IntStream.range(0, size).mapToObj(i -> generator.get())
				.collect(Collectors.toList());
	}

	public T getIndividual(int index) {
		return individuals.get(index);
	}

	public int size() {
		return individuals.size();
	}

	public T bestIndividual() {
		return individuals.stream()
				.max(Comparator.comparingDouble(Individual::fitness)).get();
	}

	public List<T> asList() {
		return Collections.unmodifiableList(individuals);
	}

	public List<T> asSortedList() {
		List<T> sorted = new ArrayList<>(individuals);
		sorted.sort(Comparator.comparingDouble(Individual::fitness).reversed());
		return Collections.unmodifiableList(sorted);
	}

	public Stream<T> asStream() {
		return individuals.stream();
	}

	public void replaceWith(Collection<T> individuals)
			throws PopulationSizeException {
		checkSize(individuals);
		this.individuals.clear();
		this.individuals.addAll(individuals);
	}

	private void checkSize(Collection<T> individuals)
			throws PopulationSizeException {
		if (individuals.size() != this.individuals.size())
			throw new PopulationSizeException(this.individuals.size(),
					individuals.size());
	}

	@Override
	public String toString() {
		return Joiner.join(asStream().map(T::toString), "\n\n");
	}

}
