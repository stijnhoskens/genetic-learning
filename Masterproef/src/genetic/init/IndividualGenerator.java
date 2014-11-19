package genetic.init;

import genetic.individuals.Individual;

@FunctionalInterface
public interface IndividualGenerator<T extends Individual> {

	T get();

}
