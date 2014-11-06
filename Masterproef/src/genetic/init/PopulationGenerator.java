package genetic.init;

import genetic.Individual;
import genetic.Population;

@FunctionalInterface
public interface PopulationGenerator<T extends Individual> {

	Population<T> get(int populationSize);

}
