package genetic.selection;

import genetic.Individual;
import genetic.Population;

import java.util.List;

@FunctionalInterface
public interface SelectionStrategy<T extends Individual> {

	List<T> selectionOf(Population<T> population);
	
}
