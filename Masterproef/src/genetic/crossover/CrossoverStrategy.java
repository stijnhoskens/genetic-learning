package genetic.crossover;

import util.Pair;

public interface CrossoverStrategy<T> {

	Pair<T, T> childrenOf(Pair<T, T> parents);

}
