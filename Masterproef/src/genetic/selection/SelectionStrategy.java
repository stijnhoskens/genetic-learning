package genetic.selection;

import genetic.Population;
import genetic.individuals.Individual;

import java.util.List;

@FunctionalInterface
public interface SelectionStrategy<T extends Individual> {

	List<T> selectionOf(Population<T> population, double elitist);

}
