package genetic.individuals;

import genetic.individuals.rules.RuleList;
import genetic.individuals.rules.RuledIndividual;
import genetic.init.IndividualGenerator;
import genetic.init.RandomGenerator;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import learning.Classifiers;
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
			System.out.print("Evaluating ");
			System.out.print(clsfr);
			System.out.print(" on ");
			System.out.println(features.getDataSet());
			return evaluate(clsfr, features.getDataSet());
		}).average().orElse(0);
	}

	private static void fillCache() {
		Set<Features> features = DataSet.trainingSets().map(Features::load)
				.collect(Collectors.toSet());
		Evaluator eval = new Evaluator();
		IndividualGenerator<RuledIndividual> gen = new RandomGenerator(
				features, eval);
		while (true) {
			long time = System.currentTimeMillis();
			gen.get().fitness();
			long elapsed = System.currentTimeMillis() - time;
			System.out.print("Elapsed time: ");
			System.out.println(elapsed);
		}
	}

	public static void main(String[] args) {
		fillCache();
	}
}
