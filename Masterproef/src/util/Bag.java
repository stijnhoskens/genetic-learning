package util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Bag<T> {

	public final Map<T, Integer> map = new HashMap<>();

	public Bag() {

	}

	private Bag(Bag<T> oldBag) {
		addAllInternal(oldBag);
	}

	public void add(T t) {
		addInternal(t);
	}

	public void addAll(Bag<T> bag) {
		addAllInternal(bag);
	}

	public int count(T t) {
		return map.getOrDefault(t, 0);
	}

	public Stream<T> stream() {
		return map.keySet().stream().flatMap(k -> {
			@SuppressWarnings("unchecked")
			T[] duplicates = (T[]) new Object[map.get(k)];
			Arrays.fill(duplicates, k);
			return Arrays.stream(duplicates);
		});
	}

	public static <T> Collector<T, Bag<T>, Bag<T>> collector() {
		return Collector.of(Bag::new, (b, t) -> b.add(t), (b1, b2) -> {
			b1.addAll(b2);
			return b1;
		}, Characteristics.IDENTITY_FINISH, Characteristics.UNORDERED,
				Characteristics.CONCURRENT);
	}

	public Bag<T> unmodifiable() {
		return new Bag<T>(this) {
			@Override
			public void add(T t) {
				throw new UnsupportedOperationException();
			}

			@Override
			public void addAll(Bag<T> bag) {
				throw new UnsupportedOperationException();
			}

			@Override
			public void clear() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override
	public String toString() {
		return map.keySet().stream().map(k -> k.toString() + ": " + map.get(k))
				.collect(Collectors.joining("\n", "[", "]"));
	}

	public void clear() {
		map.clear();
	}

	public void addInternal(T t) {
		map.merge(t, 1, (x, y) -> x + y);
	}

	private void addAllInternal(Bag<T> bag) {
		bag.stream().forEach(this::addInternal);
	}
}
