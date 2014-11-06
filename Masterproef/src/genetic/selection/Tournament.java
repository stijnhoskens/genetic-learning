package genetic.selection;

import java.util.Random;

import genetic.Population;
import genetic.individuals.Individual;

public class Tournament<T extends Individual> {

	private final Population<T> pop;
	private final Random r;

	public Tournament(Population<T> population, Random random) {
		r = random;
		pop = population;
	}

	public Tournament(Population<T> population) {
		this(population, new Random());
	}

	public T nextWinner() {
		T player1 = pop.getIndividual(r.nextInt(pop.size()));
		T player2 = pop.getIndividual(r.nextInt(pop.size()));
		return player1.fitness() > player2.fitness() ? player1 : player2;
	}
}
