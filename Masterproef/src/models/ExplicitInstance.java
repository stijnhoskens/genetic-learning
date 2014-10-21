package models;

import java.util.List;
import java.util.function.Function;

public class ExplicitInstance extends AbstractInstance<WordFrequencyPair> {

	/**
	 * @pre words == sorted
	 */
	public ExplicitInstance(String topic, List<WordFrequencyPair> words) {
		super(topic, words);
	}

	public ExplicitInstance(String topic, List<WordFrequencyPair> words,
			boolean isSorted) {
		super(topic, words, isSorted);
	}

	/**
	 * @pre words == sorted
	 */
	public ExplicitInstance(String line) {
		super(line);
	}

	public ExplicitInstance(String line, boolean isSorted) {
		super(line, isSorted);
	}

	@Override
	protected Function<String, WordFrequencyPair> constructT() {
		return WordFrequencyPair::new;
	}

}
