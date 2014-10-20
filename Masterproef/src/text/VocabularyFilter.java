package text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.function.Predicate;

import models.WordFrequencyPair;
import data.DataSet;
import data.IO;

public class VocabularyFilter {

	private static Predicate<WordFrequencyPair> filter = wfp -> !Stopwords
			.contains(wfp.getWord())
			&& wfp.getFrequency() > 2
			&& !wfp.chars().allMatch(c -> !Character.isDigit(c));

	public static void filter(DataSet data) throws IOException {
		Path voc_freq = data.vocFreq();
		Path voc = data.voc();
		// Dirty hack, try this without using a temporary file which is an exact
		// copy of voc_freq, and is read-only.
		Path temp = Files.createTempFile(voc_freq.getParent(), null, null);
		Files.copy(voc_freq, temp, StandardCopyOption.REPLACE_EXISTING);
		Path temp1 = Files.createTempFile(voc_freq.getParent(),
				"voc_freq_temp", null);
		IO.write(
				temp1,
				w -> {
					Vocabulary.WithFrequency.loadStream(temp).filter(filter)
							.map(WordFrequencyPair::toString)
							.forEach(s -> IO.writeLine(w, s));
				});
		Path temp2 = Files.createTempFile(voc.getParent(), "voc_temp", null);
		IO.write(
				temp2,
				w -> {
					Vocabulary.WithFrequency.loadStream(temp1)
							.map(WordFrequencyPair::getWord)
							.forEach(s -> IO.writeLine(w, s));
				});
		IO.replace(temp1, voc_freq);
		IO.replace(temp2, voc);
		Files.delete(temp);
	}

	public static void main(String[] args) throws IOException {
		filter(DataSet.MOVIES);
	}

}
