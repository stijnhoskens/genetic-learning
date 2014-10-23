package pre.text;

import io.IO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

import models.DataSet;
import models.ExplicitInstance;
import models.IndexFrequencyPair;
import models.Instance;
import models.Vocabulary;
import models.WordFrequencyPair;

public class WordIndexer {

	private static Vocabulary voc;

	public static void index(DataSet data) throws IOException {
		voc = Vocabulary.load(data);
		indexPath(data.testExplicit(), data.test());
		indexPath(data.trainExplicit(), data.train());
	}

	private static void indexPath(Path source, Path target) throws IOException {
		Files.createFile(target);
		IO.write(
				target,
				w -> {
					IO.lines(source).forEach(
							l -> IO.writeLine(w,
									toIndexed(new ExplicitInstance(l))
											.toString()));
				});
	}

	private static IndexFrequencyPair toIndexed(WordFrequencyPair pair) {
		return new IndexFrequencyPair(voc.indexOf(pair.getWord()),
				pair.getFrequency());
	}

	private static Instance toIndexed(ExplicitInstance instance) {
		return new Instance(instance.getTopic(), instance.getWords().stream()
				.map(WordIndexer::toIndexed).collect(Collectors.toList()));
	}

	public static void main(String[] args) throws IOException {
		DataSet[] datasets = { DataSet.CLASSIC, DataSet.MOVIES, DataSet.R52,
				DataSet.RCV1, DataSet.WEBKB, DataSet.WIPO };
		Arrays.stream(datasets).forEach(data -> {
			try {
				WordIndexer.index(data);
				System.out.print(data.toString());
				System.out.println(" indexing complete.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
