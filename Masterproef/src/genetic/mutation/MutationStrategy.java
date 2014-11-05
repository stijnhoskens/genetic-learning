package genetic.mutation;

import genetic.Individual;

public interface MutationStrategy<T extends Individual> {

	void mutate(T individual);

}
