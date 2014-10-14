package text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Dictionary extends AbstractDictionary<String> {

	private Dictionary(Collection<String> words,
			Comparator<? super String> comp, boolean isSorted) {
		super(words, comp, isSorted);
	}

	public Dictionary(Collection<String> words) {
		super(words, naturalOrder(), false);
	}

	/**
	 * Builds a dictionary using the file at the given path, in the format where
	 * every line is the category followed by each word in the form
	 * "word:frequency".
	 */
	public static Dictionary build(Path path) throws IOException {
		return new Dictionary(wordFreqPairsOf(path).map(
				WordFrequencyPair::getWord).collect(Collectors.toSet()),
				naturalOrder(), false);
	}

	public static Dictionary load(Path path) throws IOException {
		return new Dictionary(Files.readAllLines(path), naturalOrder(), true);
	}

	private static Stream<WordFrequencyPair> wordFreqPairsOf(Path path)
			throws IOException {
		return Files.lines(path).map(s -> s.substring(s.indexOf(' ') + 1))
				.flatMap(l -> Arrays.stream(l.split(" ")))
				.map(WordFrequencyPair::new);
	}

	private static <T extends Comparable<T>> Comparator<T> naturalOrder() {
		return Comparator.naturalOrder();
	}

	public static class WithFrequency extends
			AbstractDictionary<WordFrequencyPair> {

		private WithFrequency(Collection<WordFrequencyPair> terms,
				Comparator<? super WordFrequencyPair> comp, boolean isSorted) {
			super(terms, comp, isSorted);
		}

		public WithFrequency(Collection<WordFrequencyPair> words) {
			super(words, naturalOrder(), false);
		}

		public static WithFrequency build(Path path) throws IOException {
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

		public static WithFrequency load(Path path) throws IOException {
			return new WithFrequency(Files.readAllLines(path).stream()
					.map(WordFrequencyPair::new).collect(Collectors.toSet()),
					naturalOrder(), true);
		}
	}

	public static void main(String[] args) throws IOException {
		WithFrequency d = WithFrequency.build(Paths
				.get("C:\\Users\\User\\git\\Masterproef\\"
						+ "Masterproef\\datasets\\rcv1\\rcv1.txt"));
		d.export(Paths.get("C:\\Users\\User\\git\\Masterproef\\"
				+ "Masterproef\\datasets\\rcv1\\dictionarywf.txt"));
	}
}
