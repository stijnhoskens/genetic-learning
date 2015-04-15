package analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import util.Bag;
import datasets.stats.DoubleDistribution;
import datasets.stats.IntDistribution;

public class BagStatistics<T> {

	private List<Bag<T>> bags = new ArrayList<>();

	public void accept(Bag<T> bag) {
		bags.add(bag);
	}

	public Map<T, IntDistribution> keyDistributions() {
		int n = bags.size();
		Map<T, int[]> map = bags.stream().flatMap(Bag::keys).distinct()
				.collect(Collectors.toMap(k -> k, k -> new int[n]));
		IntStream.range(0, n).forEach(i -> {
			map.keySet().forEach(k -> map.get(k)[i] = bags.get(i).count(k));
		});
		return map
				.keySet()
				.stream()
				.collect(
						Collectors.toMap(k -> k,
								k -> new IntDistribution(map.get(k))));
	}

	public Map<T, DoubleDistribution> normalized() {
		Map<T, IntDistribution> keyDistr = keyDistributions();
		if (keyDistr.isEmpty())
			return Collections.emptyMap();
		int n = keyDistr.entrySet().stream().findAny().get().getValue().size();
		int[] totals = new int[n];
		keyDistr.entrySet()
				.stream()
				.map(Entry::getValue)
				.forEach(
						id -> IntStream.range(0, n).forEach(
								i -> totals[i] += id.get(i)));
		Map<T, DoubleDistribution> norm = new HashMap<>();
		keyDistr.entrySet()
				.stream()
				.forEach(
						e -> norm.put(e.getKey(), e.getValue()
								.normalize(totals)));
		return norm;

	}

	@Override
	public String toString() {
		return normalized()
				.entrySet()
				.stream()
				.sorted((e0, e1) -> Double.compare(e1.getValue().average(), e0
						.getValue().average()))
				.map(e -> e.getKey().toString() + " - avg:"
						+ e.getValue().average() + ", std:"
						+ e.getValue().standardDeviation())
				.collect(Collectors.joining("\n", "[", "]"));
	}
}
