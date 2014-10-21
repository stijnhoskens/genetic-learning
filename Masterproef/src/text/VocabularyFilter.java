package text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import parsers.InstancesParser;
import models.ExplicitInstance;
import models.WordFrequencyPair;
import data.DataSet;
import data.ExplicitDataSorter;
import data.IO;

public class VocabularyFilter {

	private static Predicate<WordFrequencyPair> filter = wfp -> !Stopwords
			.contains(wfp.getWord())
			&& wfp.getFrequency() > 2
			&& !wfp.getWord().chars().anyMatch(c -> Character.isDigit(c));

	public static void filter(DataSet data) throws IOException {

		Path voc_freq = data.vocFreq();
		Path voc = data.voc();

		Path temp1 = Files.createTempFile(voc_freq.getParent(),
				"voc_freq_temp", null);
		final Stream<WordFrequencyPair> stream = Vocabulary.WithFrequency
				.loadStream(voc_freq);
		IO.write(temp1,
				w -> stream.filter(filter).map(WordFrequencyPair::toString)
						.forEach(s -> IO.writeLine(w, s)));
		stream.close();

		Path temp2 = Files.createTempFile(voc.getParent(), "voc_temp", null);
		final Stream<WordFrequencyPair> stream2 = Vocabulary.WithFrequency
				.loadStream(temp1);
		IO.write(
				temp2,
				w -> stream2.map(WordFrequencyPair::getWord).forEach(
						s -> IO.writeLine(w, s)));
		stream2.close();

		IO.replace(temp1, voc_freq);
		IO.replace(temp2, voc);

		Vocabulary vocabulary = Vocabulary.load(data.voc());
		filterInstances(data.trainExplicit(), vocabulary);
		filterInstances(data.testExplicit(), vocabulary);
	}

	private static void filterInstances(Path path, Vocabulary voc)
			throws IOException {
		Path tempPath = Files.createTempFile(path.getParent(), null, null);
		Files.copy(path, tempPath, StandardCopyOption.REPLACE_EXISTING);
		Path temp = Files.createTempFile(path.getParent(), null, null);
		IO.write(
				temp,
				w -> InstancesParser.parseExplicit(tempPath).forEach(
						i -> IO.writeLine(
								w,
								new ExplicitInstance(i.getTopic(), i
										.getWords()
										.stream()
										.filter(wfp -> voc.contains(wfp
												.getWord()))
										.collect(Collectors.toList()))
										.toString())));
		IO.replace(temp, path);
		Files.delete(tempPath);
	}

	public static void main(String[] args) throws IOException {
		DataSet data = DataSet.WIPO;
		filter(data);
		ExplicitDataSorter.sort(data);
	}

}
