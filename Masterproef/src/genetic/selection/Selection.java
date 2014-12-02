package genetic.selection;

import genetic.individuals.Individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Selection {

	public static <T extends Individual> SelectionStrategy<T> getStrategy(
			String name) {
		switch (name) {
		case "SUS":
			return SUS();
		case "tournament":
			return tournament();
		case "rouletteWheel":
			return rouletteWheel();
		case "ranked":
			return ranked();
		case "random":
			return random();
		default:
			return SUS();
		}
	}

	public static <T extends Individual> SelectionStrategy<T> SUS() {
		return (pop, elitist) -> {
			int size = (int) ((1 - elitist) * pop.size());
			List<T> individuals = new ArrayList<>();
			individuals.addAll(new RouletteWheel<>(pop).universalSpin(size));
			Collections.shuffle(individuals);
			return individuals;
		};
	}

	public static <T extends Individual> SelectionStrategy<T> tournament() {
		return (pop, elitist) -> {
			int size = (int) ((1 - elitist) * pop.size());
			List<T> individuals = new ArrayList<>();
			Tournament<T> tournament = new Tournament<>(pop);
			while (individuals.size() < size)
				individuals.add(tournament.nextWinner());
			Collections.shuffle(individuals);
			return individuals;
		};
	}

	public static <T extends Individual> SelectionStrategy<T> rouletteWheel() {
		return (pop, elitist) -> {
			int size = (int) ((1 - elitist) * pop.size());
			List<T> individuals = new ArrayList<>();
			RouletteWheel<T> wheel = new RouletteWheel<>(pop);
			while (individuals.size() < size)
				individuals.add(wheel.spin());
			Collections.shuffle(individuals);
			return individuals;
		};
	}

	public static <T extends Individual> SelectionStrategy<T> ranked() {
		return (pop, elitist) -> {
			int size = (int) ((1 - elitist) * pop.size());
			List<T> individuals = new ArrayList<>();
			List<T> ranked = pop.asSortedList();
			RouletteWheel<T> wheel = new RouletteWheel<>(pop, i -> pop.size()
					- ranked.indexOf(i));
			while (individuals.size() < size)
				individuals.add(wheel.spin());
			Collections.shuffle(individuals);
			return individuals;
		};
	}

	public static <T extends Individual> SelectionStrategy<T> random() {
		return (pop, elitist) -> {
			double random = new Random().nextDouble();
			SelectionStrategy<T> selection;
			if (random < 1d / 4d)
				selection = SUS();
			else if (random < 1d / 2d)
				selection = tournament();
			else if (random < 3d / 4d)
				selection = rouletteWheel();
			else
				selection = ranked();
			return selection.selectionOf(pop, elitist);
		};
	}

}
