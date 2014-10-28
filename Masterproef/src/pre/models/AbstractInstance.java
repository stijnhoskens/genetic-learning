package pre.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

abstract class AbstractInstance<T extends Comparable<T>> {

	protected final String topic;
	protected final List<T> words;

	/**
	 * @pre words == sorted
	 */
	public AbstractInstance(String topic, List<T> words) {
		this(topic, words, true);
	}

	public AbstractInstance(String topic, List<T> words, boolean isSorted) {
		this.topic = topic;
		this.words = new ArrayList<>(words);
		if (!isSorted)
			words.sort(Comparator.naturalOrder());
	}

	/**
	 * @pre words == sorted
	 */
	public AbstractInstance(String line) {
		this(line, true);
	}

	public AbstractInstance(String line, boolean isSorted) {
		String[] array = line.split(" ");
		this.topic = array[0];
		words = array.length == 1 ? Collections.emptyList() : Arrays
				.stream(array, 1, array.length).map(constructT())
				.collect(Collectors.toList());
		if (!isSorted)
			words.sort(Comparator.naturalOrder());
	}

	public String getTopic() {
		return topic;
	}

	public List<T> getWords() {
		return Collections.unmodifiableList(words);
	}

	@Override
	public String toString() {
		String string = topic;
		for (T wfp : words)
			string += " " + wfp.toString();
		return string;
	}

	protected abstract Function<String, T> constructT();

}
