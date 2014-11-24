package util;

import java.util.stream.Stream;

public class Joiner {

	public static String join(Stream<String> elements, String delimiter) {
		StringBuilder sb = new StringBuilder();
		elements.forEach(s -> sb.append(s).append(delimiter));
		if (sb.length() == 0)
			return "";
		sb.delete(sb.length() - delimiter.length(), sb.length());
		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println(join(Stream.of("a", "b", "c"), " AND "));
	}

}
