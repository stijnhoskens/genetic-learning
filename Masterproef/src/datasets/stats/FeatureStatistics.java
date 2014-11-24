package datasets.stats;

import io.IO;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import datasets.DataSet;

public class FeatureStatistics {

	private static final Path PATH = Paths.get("datasets/stats.txt");
	private static final FeatureStatistics instance = load();
	private final double[] means, stds;
	private final Random random = new Random();

	private FeatureStatistics(double[] means, double[] stds) {
		this.means = Arrays.copyOf(means, means.length);
		this.stds = Arrays.copyOf(stds, stds.length);
	}

	public double getMean(int i) {
		return means[i];
	}

	public double getStd(int i) {
		return stds[i];
	}

	public int nbOfFeatures() {
		return means.length;
	}

	public double getSample(int i) {
		double term = random.nextGaussian() * getStd(i);
		if (-term > getMean(i))
			return getMean(i) - term;
		else
			return getMean(i) + term;
	}

	public void export() {
		IO.write(
				PATH,
				w -> IntStream.range(0, means.length).forEach(
						i -> IO.writeLine(w, means[i] + " " + stds[i])));

	}

	public static FeatureStatistics build() {
		Set<double[]> arrays = DataSet.trainingSets().map(Features::load)
				.map(Features::asArray).collect(Collectors.toSet());
		int nbOfFeatures = arrays.iterator().next().length;
		List<DoubleDistribution> dds = IntStream
				.range(0, nbOfFeatures)
				.mapToObj(i -> arrays.stream().mapToDouble(a -> a[i]).toArray())
				.map(DoubleDistribution::new).collect(Collectors.toList());
		double[] means = new double[dds.size()];
		double[] stds = new double[dds.size()];
		for (int i = 0; i < dds.size(); i++) {
			DoubleDistribution dd = dds.get(i);
			means[i] = dd.frequencyStats().getAverage();
			stds[i] = dd.standardDeviation();
		}
		return new FeatureStatistics(means, stds);

	}

	public static FeatureStatistics load() {
		List<String> lines = IO.allLines(PATH);
		double[] means = new double[lines.size()];
		double[] stds = new double[lines.size()];
		IntStream.range(0, lines.size()).forEach(
				i -> {
					double[] values = Arrays.stream(lines.get(i).split(" "))
							.mapToDouble(Double::valueOf).toArray();
					means[i] = values[0];
					stds[i] = values[1];
				});
		return new FeatureStatistics(means, stds);
	}

	public static FeatureStatistics get() {
		return instance;
	}

	public static void main(String[] args) {
		build().export();
	}
}
