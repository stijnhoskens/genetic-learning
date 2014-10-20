package text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import data.DataPath;
import data.IO;

public class VocabularyFilter {

	/**
	 * @param voc_freq
	 *            the path of voc_freq.txt to filter
	 * @param voc
	 *            the path to voc.txt
	 */
	public static void filter(Path voc_freq, Path voc) throws IOException {
		Path temp = Files.createTempFile(voc_freq.getParent(), null, null);
		IO.write(
				temp,
				w -> {
					Vocabulary.WithFrequency
							.loadStream(voc_freq)
							.filter(wfp -> !Stopwords.contains(wfp.getWord())
									&& wfp.getFrequency() > 2)
							.forEach(wfp -> IO.writeLine(w, wfp.toString()));
				});
		Path temp2 = Files.createTempFile(voc_freq.getParent(), null, null);
		IO.write(temp2, w -> {
			IO.lines(temp).map(s -> new WordFrequencyPair(s).getWord())
					.forEach(s -> IO.writeLine(w, s));
		});
		IO.replace(temp, voc_freq);
		IO.replace(temp2, voc);
	}

	public static void main(String[] args) throws IOException {
		filter(DataPath.TWENTY_NG_VOC_FREQ, DataPath.TWENTY_NG_VOC);
	}

}
