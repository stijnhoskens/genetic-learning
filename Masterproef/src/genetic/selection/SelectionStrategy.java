package genetic.selection;

import genetic.Individual;
import genetic.Population;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@FunctionalInterface
public interface SelectionStrategy<T extends Individual> {

	List<T> selectionOf(Population<T> population);

	public static <T extends Individual> SelectionStrategy<T> getStrategy(
			String name) {
		switch (name) {
		case "SUS":
			return SUS();
		case "tournament":
			return tournament();
		case "rouletteWheel":
			return rouletteWheel();
		case "random":
			return random();
		default:
			return random();
		}
	}

	public static <T extends Individual> SelectionStrategy<T> SUS() {
		return pop -> {
			List<T> individuals = new RouletteWheel<>(pop).universalSpin();
			Collections.shuffle(individuals);
			return individuals;
		};
	}

	public static <T extends Individual> SelectionStrategy<T> tournament() {
		return pop -> {
			List<T> individuals = new ArrayList<>();
			Tournament<T> tournament = new Tournament<>(pop);
			while (individuals.size() < pop.size())
				individuals.add(tournament.nextWinner());
			Collections.shuffle(individuals);
			return individuals;
		};
	}

	public static <T extends Individual> SelectionStrategy<T> rouletteWheel() {
		return pop -> {
			List<T> individuals = new ArrayList<>();
			RouletteWheel<T> wheel = new RouletteWheel<>(pop);
			while (individuals.size() < pop.size())
				individuals.add(wheel.spin());
			Collections.shuffle(individuals);
			return individuals;
		};
	}

	public static <T extends Individual> SelectionStrategy<T> random() {
		return pop -> {
			double random = new Random().nextDouble();
			SelectionStrategy<T> selection;
			if (random < 1d / 3d)
				selection = SUS();
			else if (random < 2d / 3d)
				selection = tournament();
			else
				selection = rouletteWheel();
			return selection.selectionOf(pop);
		};
	}

}
