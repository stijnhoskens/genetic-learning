package genetic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import exceptions.PopulationSizeException;

public class Population {

	private final List<Individual> individuals;

	public Population(Collection<Individual> individuals) {
		this.individuals = new ArrayList<>(individuals);
	}

	public Individual bestIndividual() {
		return individuals.stream()
				.max(Comparator.comparingDouble(Individual::fitness)).get();
	}

	public void replaceWith(Collection<Individual> individuals)
			throws PopulationSizeException {
		checkSize(individuals);
		this.individuals.clear();
		this.individuals.addAll(individuals);
	}

	public void checkSize(Collection<Individual> individuals)
			throws PopulationSizeException {
		if (individuals.size() != this.individuals.size())
			throw new PopulationSizeException(this.individuals.size(),
					individuals.size());
	}

}
