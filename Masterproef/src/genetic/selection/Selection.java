package genetic.selection;

import genetic.Population;
import genetic.individuals.Individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Selection {

	public static <T extends Individual> SelectionStrategy<T> getStrategy(
			String name, double elitism) {
		switch (name) {
		case "SUS":
			return SUS(elitism);
		case "tournament":
			return tournament(elitism);
		case "rouletteWheel":
			return rouletteWheel(elitism);
		case "ranked":
			return ranked(elitism);
		case "random":
			return random(elitism);
		default:
			return SUS(elitism);
		}
	}

	public static <T extends Individual> SelectionStrategy<T> SUS(double elitism) {
		return pop -> {
			List<T> individuals = withElite(pop, elitism);
			individuals.addAll(new RouletteWheel<>(pop).universalSpin(pop
					.size() - individuals.size()));
			Collections.shuffle(individuals);
			return individuals;
		};
	}

	public static <T extends Individual> SelectionStrategy<T> tournament(
			double elitism) {
		return pop -> {
			List<T> individuals = withElite(pop, elitism);
			Tournament<T> tournament = new Tournament<>(pop);
			while (individuals.size() < pop.size())
				individuals.add(tournament.nextWinner());
			Collections.shuffle(individuals);
			return individuals;
		};
	}

	public static <T extends Individual> SelectionStrategy<T> rouletteWheel(
			double elitism) {
		return pop -> {
			List<T> individuals = withElite(pop, elitism);
			RouletteWheel<T> wheel = new RouletteWheel<>(pop);
			while (individuals.size() < pop.size())
				individuals.add(wheel.spin());
			Collections.shuffle(individuals);
			return individuals;
		};
	}

	public static <T extends Individual> SelectionStrategy<T> ranked(
			double elitism) {
		return pop -> {
			List<T> individuals = withElite(pop, elitism);
			List<T> ranked = pop.asSortedList();
			RouletteWheel<T> wheel = new RouletteWheel<>(pop, i -> pop.size()
					- ranked.indexOf(i));
			while (individuals.size() < pop.size())
				individuals.add(wheel.spin());
			Collections.shuffle(individuals);
			return individuals;
		};
	}

	public static <T extends Individual> SelectionStrategy<T> random(
			double elitism) {
		return pop -> {
			double random = new Random().nextDouble();
			SelectionStrategy<T> selection;
			if (random < 1d / 4d)
				selection = SUS(elitism);
			else if (random < 1d / 2d)
				selection = tournament(elitism);
			else if (random < 3d / 4d)
				selection = rouletteWheel(elitism);
			else
				selection = ranked(elitism);
			return selection.selectionOf(pop);
		};
	}

	private static <T extends Individual> List<T> withElite(Population<T> pop,
			double ratio) {
		return new ArrayList<>(pop.asSortedList().subList(0,
				(int) (pop.size() * ratio)));
	}

}
