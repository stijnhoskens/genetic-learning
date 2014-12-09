package genetic.individuals;

import genetic.individuals.rules.RuleList;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import learning.Classifiers;
import util.Joiner;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import datasets.DataSet;
import datasets.stats.Features;

public class Evaluator {

	private final Metric metric;
	private static final Path CACHE_PATH = Paths.get("datasets/cache.txt");
	private final Properties cache;

	public Evaluator(Metric metric) {
		this.metric = metric;
		cache = new Properties();
		try {
			cache.load(Files.newInputStream(CACHE_PATH));
		} catch (IOException e) {
			// do nothing, an empty properties object will be returned.
		}
	}

	public Evaluator() {
		this(Metric.PROCENT_CORRECT);
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
			String key = clsfrName + "@" + data.toString();
			if (cache.containsKey(key))
				return Double.valueOf(cache.getProperty(key));
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
			putProperty(key, evaluation);
			return metric.value(eval);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	private void putProperty(String key, double evaluation) {
		cache.setProperty(key, String.valueOf(evaluation));
		OutputStream output;
		try {
			output = Files.newOutputStream(CACHE_PATH);
			cache.store(output, null);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public double evaluate(RuleList rList, Set<Features> data) {
		return data.stream().mapToDouble(features -> {
			String clsfr = rList.apply(features);
			return evaluate(features, clsfr);
		}).average().orElse(0);
	}

	private double evaluate(Features f, String c) {
		return evaluate(c, f.getDataSet());
	}

	protected static void fillCache() {
		Set<Features> features = DataSet.trainingSets().map(Features::load)
				.collect(Collectors.toSet());
		Evaluator eval = new Evaluator();
		ExecutorService threadpool = Executors.newFixedThreadPool(2);
		features.forEach(f -> Classifiers.allOptions().forEach(
				s -> threadpool.execute(() -> eval.evaluate(f, s))));
	}

	public static void cleanCache() {
		Evaluator eval = new Evaluator();
		Properties cache = eval.cache;
		Properties newCache = new Properties();
		cache.entrySet().forEach(e -> {
			String key = (String) e.getKey();
			if (!key.startsWith("SVM")) {
				newCache.setProperty(key, (String) e.getValue());
				return;
			}
			String[] tokens = key.split("@");
			String clsfr = tokens[0];
			String[] splitted = clsfr.split(" ");
			if (splitted[1].equals("-S")) {
				swap(splitted, 1, 3);
				swap(splitted, 2, 4);
			}
			clsfr = Joiner.join(" ", splitted);
			tokens[0] = clsfr;
			key = Joiner.join("@", tokens);
			newCache.setProperty(key, (String) e.getValue());
		});
		OutputStream output;
		try {
			output = Files.newOutputStream(CACHE_PATH);
			newCache.store(output, null);
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void swap(String[] tokens, int i, int j) {
		String temp = tokens[i];
		tokens[i] = tokens[j];
		tokens[j] = temp;
	}

	public static void main(String[] args) {
		fillCache();
	}
}
