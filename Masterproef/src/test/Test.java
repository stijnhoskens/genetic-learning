package test;

import java.util.Comparator;

import datasets.DataSet;
import util.Pair;
import genetic.individuals.Evaluator;
import learning.Classifiers;

public class Test {

	public static void main(String[] args) throws Exception {

		Evaluator eval = new Evaluator();
		Classifiers.allOptions()
				.map(s -> new Pair<>(s, DataSet.all().mapToDouble(ds -> {
					return eval.evaluate(s, ds);
				}).average().orElse(0)))
				.sorted(Comparator.comparing(Pair::getSecond))
				.forEach(System.out::println);

	}
}
