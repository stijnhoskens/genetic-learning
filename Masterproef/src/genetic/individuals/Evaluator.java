package genetic.individuals;

import genetic.individuals.rules.RuleList;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import learning.Classifiers;
import util.Pair;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import datasets.DataSet;
import datasets.stats.Features;

public class Evaluator {

	private final Metric metric;
	private final Map<Pair<String, DataSet>, Double> cache = new HashMap<>();

	public Evaluator(Metric metric) {
		this.metric = metric;
	}

	public Evaluator() {
		metric = Metric.PROCENT_CORRECT;
	}

	public enum Metric {

		PROCENT_CORRECT {
			@Override
			protected double value(Evaluation eval) {
				return eval.pctCorrect();
			}
		};

		protected abstract double value(Evaluation eval);
	}

	public double evaluate(String clsfrName, DataSet data) {
		try {
			Pair<String, DataSet> pair = new Pair<>(clsfrName, data);
			if (cache.containsKey(pair))
				return cache.get(pair);
			Instances train = new DataSource(data.train().toString())
					.getDataSet();
			train.setClassIndex(train.numAttributes() - 1);
			Classifier classifier = Classifiers.get(clsfrName);
			classifier.buildClassifier(train);
			Instances test = new DataSource(data.test().toString())
					.getDataSet();
			test.setClassIndex(test.numAttributes() - 1);
			Evaluation eval = new Evaluation(train);
			eval.evaluateModel(classifier, test);
			double evaluation = metric.value(eval);
			cache.put(pair, evaluation);
			return metric.value(eval);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public double evaluate(RuleList rList, Set<Features> data) {
		return data.stream().mapToDouble(features -> {
			String clsfr = rList.apply(features);
			return evaluate(clsfr, features.getDataSet());
		}).average().orElse(0);
	}

	public Map<Pair<String, DataSet>, Double> flushCache() {
		Map<Pair<String, DataSet>, Double> copy = new HashMap<>(cache);
		cache.clear();
		return copy;
	}
}
