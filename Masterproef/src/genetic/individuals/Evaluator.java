package genetic.individuals;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import learning.Classifiers;
import util.Joiner;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import datasets.DataSet;
import datasets.stats.Features;

public class Evaluator {

	private final Metric metric;
	private static final Path CACHE_PATH = Paths.get("datasets/cache.txt");
	private final Properties cache;
	private final AtomicInteger nbOfEvaluations = new AtomicInteger();

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
			nbOfEvaluations.incrementAndGet();
			synchronized (this) {
				if (cache.containsKey(key))
					return Double.valueOf(cache.getProperty(key));
			}
			// System.out.println("Evaluating " + clsfrName + " on " + data +
			// ".");
			// LocalTime start = LocalTime.now();
			Instances train = data.trainInstances();
			Classifier classifier = Classifiers.trained(clsfrName, train);
			Instances test = data.testInstances();
			Evaluation eval = new Evaluation(train);
			eval.evaluateModel(classifier, test);
			double evaluation = metric.value(eval);
			// long seconds = Duration.between(start, LocalTime.now())
			// .getSeconds();
			// System.out.println("Evaluation of " + clsfrName + " on " + data
			// + " done.");
			// System.out.println("Elapsed time: " + seconds + "s.");
			putProperty(key, evaluation);
			return metric.value(eval);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	private synchronized void putProperty(String key, double evaluation) {
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

	private double evaluate(Features f, String c) {
		return evaluate(c, f.getDataSet());
	}

	protected static void fillCache(int threadpoolSize) {
		Set<Features> features = DataSet.all().map(Features::load)
				.collect(Collectors.toSet());
		Evaluator eval = new Evaluator();
		ExecutorService threadpool = Executors
				.newFixedThreadPool(threadpoolSize);
		features.forEach(f -> Classifiers.allOptions().forEach(
				s -> threadpool.execute(() -> eval.evaluate(f, s))));
		threadpool.shutdown();
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

	public static void main(String[] args) throws IOException {
		fillCache(2);
	}

	public int getNbOfEvaluations() {
		return nbOfEvaluations.get();
	}
}
