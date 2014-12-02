package learning;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import util.Pair;

public class Parameter {

	private final String prefix;
	private final String[] values;

	public Parameter(String prefix, String[] possibleValues) {
		this.prefix = prefix;
		values = possibleValues;
	}

	public Parameter(String fullDescription) {
		Pair<String, String[]> splitted = split(fullDescription);
		prefix = splitted.getFirst();
		values = splitted.getSecond();
	}

	private static Pair<String, String[]> split(String full) {
		String[] splitted = full.split("\\|");
		return new Pair<>(splitted[0], splitted[1].split(","));
	}

	public int nbOfValues() {
		return values.length;
	}

	public String toString(int valueIndex) {
		return prefix + " " + values[valueIndex];
	}

	public Stream<String> possibleValues() {
		return IntStream.range(0, values.length).mapToObj(this::toString);
	}

	@Override
	public String toString() {
		return prefix + " " + Arrays.toString(values);
	}

}
