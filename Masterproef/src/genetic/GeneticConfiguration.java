package genetic;

public class GeneticConfiguration {

	final double mProb;
	final double coProb;
	final int popSize;
	final double elitist;

	public GeneticConfiguration(double m, double co, int size, double elite) {
		mProb = m;
		coProb = co;
		popSize = size;
		elitist = elite;
	}

}
