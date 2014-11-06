package genetic.mutation;

import genetic.individuals.Individual;

@FunctionalInterface
public interface MutationStrategy<T extends Individual> {

	T mutate(T individual);

}
