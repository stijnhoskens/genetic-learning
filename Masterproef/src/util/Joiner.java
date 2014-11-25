package util;

import static java.util.stream.Collectors.joining;

import java.util.stream.Stream;

public class Joiner {

	public static String join(Stream<String> elements, String delimiter) {
		return elements.collect(joining(delimiter));
	}

	public static String join(Stream<String> elements, String delimiter,
			String prefix, String suffix) {
		return elements.collect(joining(delimiter, prefix, suffix));
	}

	public static void main(String[] args) {
		System.out.println(join(Stream.of("a", "b", "c"), " AND "));
	}

}
