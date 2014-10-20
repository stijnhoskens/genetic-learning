package models;

import java.util.List;
import java.util.function.Function;

public class Instance extends AbstractInstance<IndexFrequencyPair> {

	/**
	 * @pre words == sorted
	 */
	public Instance(String topic, List<IndexFrequencyPair> words) {
		super(topic, words);
	}

	public Instance(String topic, List<IndexFrequencyPair> words,
			boolean isSorted) {
		super(topic, words);
	}

	/**
	 * @pre words == sorted
	 */
	public Instance(String line) {
		super(line, true);
	}

	public Instance(String line, boolean isSorted) {
		super(line);
	}

	@Override
	protected Function<String, IndexFrequencyPair> constructT() {
		return IndexFrequencyPair::new;
	}

}
