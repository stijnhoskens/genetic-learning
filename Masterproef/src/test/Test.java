package test;

import genetic.individuals.Evaluator;

import java.util.Comparator;
import java.util.Set;
import java.util.stream.Collectors;

import learning.Classifiers;
import util.Pair;
import datasets.DataSet;

public class Test {

	public static void main(String[] args) throws Exception {
		System.out.println(findBestSolution());
	}

	public static double findBestSolution() {
		Set<DataSet> datasets = DataSet.trainingSets().collect(
				Collectors.toSet());
		Evaluator eval = new Evaluator();
		double accum = 0;
		for (DataSet ds : datasets) {
			Pair<String, Double> result = Classifiers.allOptions()
					.map(s -> new Pair<>(s, eval.evaluate(s, ds)))
					.max(Comparator.comparingDouble(Pair::getSecond)).get();
			System.out.println(result.getFirst() + " @ " + ds.toString()
					+ ", score=" + result.getSecond() + ".");
			accum += result.getSecond();
		}
		return accum / (double) datasets.size();
	}
}
