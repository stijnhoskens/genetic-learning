package genetic;

import genetic.individuals.Individual;

public class GeneticAdapter<T extends Individual> extends GeneticAlgorithm<T> {

	public GeneticAdapter(double mutationP, double crossoverP,
			int populationSize, double elitist) {
		super(mutationP, crossoverP, populationSize, elitist);
	}

	public GeneticAdapter(GeneticConfiguration config) {
		super(config);
	}

	@Override
	protected void problemSpecificInitialisation() {
		// do nothing
	}

	@Override
	protected void problemSpecific() {
		// do nothing
	}

}
