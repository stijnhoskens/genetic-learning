package util;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Joiner {

	public static String join(Stream<String> elements, String delimiter) {
		return elements.collect(Collectors.joining(delimiter));
	}

	public static void main(String[] args) {
		System.out.println(join(Stream.of("a", "b", "c"), " AND "));
	}

}
