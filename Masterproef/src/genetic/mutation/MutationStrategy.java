package genetic.mutation;

import genetic.Individual;

@FunctionalInterface
public interface MutationStrategy<T extends Individual> {

	T mutate(T individual);

}
