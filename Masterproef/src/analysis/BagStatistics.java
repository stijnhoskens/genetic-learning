package analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import util.Bag;
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

	@Override
	public String toString() {
		return keyDistributions()
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
