package genetic;

public class Individual {

	private final double fitness;

	public Individual() {
		fitness = calculateFitness();
	}

	public double fitness() {
		return fitness;
	}

	/**
	 * This method is used to calculate the fitness of a certain individual only
	 * once.
	 */
	private double calculateFitness() {
		return 0; // TODO
	}

}
