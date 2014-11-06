package genetic.crossover;

import util.Pair;

@FunctionalInterface
public interface CrossoverStrategy<T> {

	Pair<T, T> childrenOf(Pair<T, T> parents);

}
