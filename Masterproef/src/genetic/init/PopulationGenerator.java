package genetic.init;

import genetic.Population;
import genetic.individuals.Individual;

@FunctionalInterface
public interface PopulationGenerator<T extends Individual> {

	Population<T> get(int populationSize);

}
