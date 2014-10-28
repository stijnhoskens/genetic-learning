package pre.models;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class TextInstance extends AbstractInstance<IndexFrequencyPair> {

	/**
	 * @pre words == sorted
	 */
	public TextInstance(String topic, List<IndexFrequencyPair> words) {
		super(topic, words);
	}

	public TextInstance(String topic, List<IndexFrequencyPair> words,
			boolean isSorted) {
		super(topic, words, isSorted);
	}

	/**
	 * @pre words == sorted
	 */
	public TextInstance(String line) {
		super(line);
	}

	public TextInstance(String line, boolean isSorted) {
		super(line, isSorted);
	}

	public int frequencyOf(int index) {
		int search = Collections.binarySearch(words, new IndexFrequencyPair(
				index, 0));
		return search >= 0 ? search : 0;
	}

	@Override
	protected Function<String, IndexFrequencyPair> constructT() {
		return IndexFrequencyPair::new;
	}

}
