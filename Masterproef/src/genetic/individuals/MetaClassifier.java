package genetic.individuals;

import java.util.Set;

import datasets.stats.DataSetFeatures;

public class MetaClassifier implements Individual {

	private final Set<DataSetFeatures> datasets;
	private double fitness = -1;
	private final DTNode initial;

	public MetaClassifier(Set<DataSetFeatures> datasets, DTNode initialNode) {
		this.datasets = datasets;
		this.initial = initialNode;
	}

	@Override
	public double fitness() {
		if (fitness >= 0)
			return fitness;
		fitness = calculateFitness();
		return fitness;
	}

	private double calculateFitness() {
		return datasets.stream().mapToDouble(initial::accuracy).sum();
	}
}
