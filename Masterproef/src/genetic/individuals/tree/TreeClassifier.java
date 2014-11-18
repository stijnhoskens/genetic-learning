package genetic.individuals.tree;

import genetic.individuals.Individual;

import java.util.Set;

import datasets.stats.DataSetFeatures;

public class TreeClassifier implements Individual {

	private final Set<DataSetFeatures> datasets;
	private final DTNode initial;
	private double fitness = -1;

	public TreeClassifier(Set<DataSetFeatures> datasets, DTNode initialNode) {
		this.datasets = datasets;
		this.initial = initialNode;
	}

	@Override
	public double fitness() {
		if (fitness < 0)
			fitness = calculateFitness();
		return fitness;
	}

	private double calculateFitness() {
		return datasets.stream().mapToDouble(initial::accuracy).sum();
	}
}
