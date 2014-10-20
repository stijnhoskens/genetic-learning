package text;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import data.DataPath;
import data.IO;

public class Vocabulary extends AbstractVocabulary<String> {

	private Vocabulary(Collection<String> words,
			Comparator<? super String> comp, boolean isSorted) {
		super(words, comp, isSorted);
	}

	public Vocabulary(Collection<String> words) {
		super(words, naturalOrder(), false);
	}

	@Override
	public int indexOf(String word) {
		return Collections.binarySearch(terms, word);
	}

	/**
	 * Builds a dictionary using the file at the given path, in the format where
	 * every line is the category followed by each word in the form
	 * "word:frequency".
	 */
	public static Vocabulary build(Path... path) {
		return new Vocabulary(wordFreqPairsOf(path).map(
				WordFrequencyPair::getWord).collect(Collectors.toSet()),
				naturalOrder(), false);
	}

	public static void export(Path output, Stream<WordFrequencyPair> stream) {

	}

	public static void buildAndExport(Path output, Path... input) {
		build(input).export(output);
	}

	public static Vocabulary load(Path path) {
		return new Vocabulary(IO.allLines(path), naturalOrder(), true);
	}

	private static Stream<WordFrequencyPair> wordFreqPairsOf(Path... path) {
		return Arrays.stream(path).flatMap(p -> IO.lines(p))
				.map(s -> s.substring(s.indexOf(' ') + 1))
				.flatMap(l -> Arrays.stream(l.split(" ")))
				.map(WordFrequencyPair::new);
	}

	private static <T extends Comparable<T>> Comparator<T> naturalOrder() {
		return Comparator.naturalOrder();
	}

	public static class WithFrequency extends
			AbstractVocabulary<WordFrequencyPair> {

		private WithFrequency(Collection<WordFrequencyPair> terms,
				Comparator<? super WordFrequencyPair> comp, boolean isSorted) {
			super(terms, comp, isSorted);
		}

		@Override
		public int indexOf(String word) {
			return Collections.binarySearch(terms, new WordFrequencyPair(word,
					0), (a0, a1) -> a0.getWord().compareTo(a1.getWord()));
		}

		public WithFrequency(Collection<WordFrequencyPair> words) {
			super(words, naturalOrder(), false);
		}

		public static WithFrequency build(Path... path) {
			return new WithFrequency(
					wordFreqPairsOf(path)
							.collect(
									Collectors
											.groupingBy(
													WordFrequencyPair::getWord,
													Collectors
															.summingInt(WordFrequencyPair::getFrequency)))
							.entrySet()
							.stream()
							.map(e -> new WordFrequencyPair(e.getKey(), e
									.getValue().intValue()))
							.collect(Collectors.toSet()), naturalOrder(), false);
		}

		public static void buildAndExport(Path output, Path... input) {
			build(input).export(output);
		}

		public static WithFrequency load(Path path) {
			return new WithFrequency(loadStream(path).collect(
					Collectors.toList()), naturalOrder(), true);
		}

		public static Stream<WordFrequencyPair> loadStream(Path path) {
			return IO.lines(path).map(WordFrequencyPair::new);
		}
	}

	public static void main(String[] args) {
		buildAndExport(DataPath.TWENTY_NG_VOC, DataPath.TWENTY_NG_TRAIN,
				DataPath.TWENTY_NG_TEST);
		WithFrequency.buildAndExport(DataPath.TWENTY_NG_VOC_FREQ,
				DataPath.TWENTY_NG_TRAIN, DataPath.TWENTY_NG_TEST);
	}
}
