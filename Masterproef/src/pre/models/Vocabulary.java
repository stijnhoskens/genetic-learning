package pre.models;

import io.IO;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import datasets.FullDataSet;

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
	public static Vocabulary build(FullDataSet data) {
		return new Vocabulary(wordFreqPairsOf(data).map(
				WordFrequencyPair::getWord).collect(Collectors.toSet()),
				naturalOrder(), false);
	}

	public static void buildAndExport(FullDataSet data) {
		build(data).export(data.voc());
	}

	public static Vocabulary load(FullDataSet data) {
		return load(data.voc());
	}

	public static Vocabulary load(Path path) {
		return new Vocabulary(IO.allLines(path), naturalOrder(), true);
	}

	public static int sizeWithoutLoading(FullDataSet data) {
		return (int) IO.lines(data.voc()).count();
	}

	private static Stream<WordFrequencyPair> wordFreqPairsOf(FullDataSet data) {
		return Stream.of(data.trainExplicit(), data.testExplicit())
				.flatMap(p -> IO.lines(p))
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

		public static WithFrequency build(FullDataSet data) {
			return new WithFrequency(
					wordFreqPairsOf(data)
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

		public static void buildAndExport(FullDataSet data) {
			build(data).export(data.vocFreq());
		}

		public static WithFrequency load(FullDataSet data) {
			return new WithFrequency(loadStream(data).collect(
					Collectors.toList()), naturalOrder(), true);
		}

		public static Stream<WordFrequencyPair> loadStream(FullDataSet data) {
			return loadStream(data.vocFreq());
		}

		public static Stream<WordFrequencyPair> loadStream(Path path) {
			return IO.lines(path).map(WordFrequencyPair::new);
		}
	}

	public static void main(String[] args) {
		buildAndExport(FullDataSet.CORA);
		WithFrequency.buildAndExport(FullDataSet.CORA);
	}
}
