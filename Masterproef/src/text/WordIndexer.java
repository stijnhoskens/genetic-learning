package text;

import java.util.stream.Collectors;

import models.IndexFrequencyPair;
import models.Instance;
import models.ExplicitInstance;
import models.WordFrequencyPair;

public class WordIndexer {

	private final Vocabulary voc;

	public WordIndexer(Vocabulary vocabulary) {
		this.voc = vocabulary;
	}

	private IndexFrequencyPair toIndexed(WordFrequencyPair pair) {
		return new IndexFrequencyPair(voc.indexOf(pair.getWord()),
				pair.getFrequency());
	}

	private Instance toIndexed(ExplicitInstance instance) {
		return new Instance(instance.getTopic(), instance.getWords().stream()
				.map(this::toIndexed).collect(Collectors.toList()));
	}

}
