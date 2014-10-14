package text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Dictionary extends AbstractDictionary<String> {

	public Dictionary(Collection<String> words) {
		super(words);
	}

	public Dictionary(Collection<String> words, Comparator<? super String> comp) {
		super(words, comp);
	}

	/**
	 * Builds a dictionary using the file at the given path, in the format where
	 * every line is the category followed by each word in the form
	 * "word:frequency".
	 */
	public static Dictionary build(Path path) throws IOException {
		return new Dictionary(wordFreqPairsOf(path).map(
				WordFrequencyPair::getWord).collect(Collectors.toSet()),
				Comparator.naturalOrder());
	}

	public static Dictionary read(Path path) throws IOException {
		return new Dictionary(Files.readAllLines(path));
	}

	private static Stream<WordFrequencyPair> wordFreqPairsOf(Path path)
			throws IOException {
		return Files.lines(path).map(s -> s.substring(s.indexOf(' ')))
				.flatMap(l -> Arrays.stream(l.split(" ")))
				.map(WordFrequencyPair::new);
	}

	public static class WithFrequency extends
			AbstractDictionary<WordFrequencyPair> {

		WithFrequency(Collection<WordFrequencyPair> terms) {
			super(terms);
		}

		WithFrequency(Collection<WordFrequencyPair> terms,
				Comparator<? super WordFrequencyPair> comp) {
			super(terms, comp);
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
							.collect(Collectors.toSet()),
					Comparator.naturalOrder());
		}

		public static WithFrequency read(Path path) throws IOException {
			return new WithFrequency(Files.readAllLines(path).stream()
					.map(WordFrequencyPair::new).collect(Collectors.toSet()));
		}
	}
}
