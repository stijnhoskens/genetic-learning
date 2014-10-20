package models;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Instance {

	private final String topic;
	private final List<WordFrequencyPair> words;

	public Instance(String topic, List<WordFrequencyPair> words) {
		this.topic = topic;
		this.words = words;
	}

	public Instance(String line) {
		String[] array = line.split(" ");
		this.topic = array[0];
		words = array.length == 1 ? Collections.emptyList() : Arrays
				.stream(Arrays.copyOfRange(array, 1, array.length))
				.map(WordFrequencyPair::new).collect(Collectors.toList());
	}

	public String getTopic() {
		return topic;
	}

	public List<WordFrequencyPair> getWords() {
		return Collections.unmodifiableList(words);
	}

}
