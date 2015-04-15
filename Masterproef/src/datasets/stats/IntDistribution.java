package datasets.stats;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class IntDistribution {

	private final int[] distribution;

	public IntDistribution(int[] distribution) {
		this.distribution = Arrays.copyOf(distribution, distribution.length);
	}

	public int get(int i) {
		return distribution[i];
	}

	public int size() {
		return distribution.length;
	}

	public IntStream asStream() {
		return Arrays.stream(distribution);
	}

	public IntSummaryStatistics frequencyStats() {
		return asStream().summaryStatistics();
	}

	public double average() {
		return frequencyStats().getAverage();
	}

	public double variance() {
		return momentAboutMean(2);
	}

	public double standardDeviation() {
		return Math.sqrt(momentAboutMean(2, true));
	}

	public double skewness() {
		return standardizedMean(3) / Math.pow(standardDeviation(), 3);
	}

	public double entropy() {
		Map<Integer, Integer> map = new LinkedHashMap<>();
		asStream().forEach(key -> {
			int value = map.containsKey(key) ? map.get(key) : 0;
			map.put(key, value + 1);
		});
		double result = 0.0;
		for (int key : map.keySet()) {
			double frequency = (double) map.get(key) / distribution.length;
			result -= frequency * (Math.log(frequency) / Math.log(2));
		}
		return result;
	}

	public DoubleDistribution normalize(int[] totals) {
		return new DoubleDistribution(IntStream
				.range(0, distribution.length)
				.mapToDouble(
						i -> ((double) distribution[i]) / ((double) totals[i]))
				.toArray());
	}

	private double momentAboutMean(int k) {
		return momentAboutMean(k, false);
	}

	private double momentAboutMean(int k, boolean shouldCorrect) {
		double avg = frequencyStats().getAverage();
		double length = shouldCorrect ? (double) distribution.length - 1
				: (double) distribution.length;
		double sum = asStream().mapToDouble(x -> ((double) x) - avg)
				.map(x -> Math.pow(x, (double) k)).sum();
		return sum / length;
	}

	private double standardizedMean(int k) {
		return momentAboutMean(k) / Math.pow(standardDeviation(), k);
	}

	@Override
	public String toString() {
		return Arrays.toString(distribution);
	}

}
