package pre.text;

import io.IO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

import pre.models.FullDataSet;
import pre.models.ExplicitInstance;
import pre.models.IndexFrequencyPair;
import pre.models.TextInstance;
import pre.models.Vocabulary;
import pre.models.WordFrequencyPair;

public class WordIndexer {

	private static Vocabulary voc;

	public static void index(FullDataSet data) throws IOException {
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

	private static TextInstance toIndexed(ExplicitInstance instance) {
		return new TextInstance(instance.getTopic(), instance.getWords().stream()
				.map(WordIndexer::toIndexed).collect(Collectors.toList()));
	}

	public static void main(String[] args) throws IOException {
		FullDataSet[] datasets = { FullDataSet.CLASSIC, FullDataSet.MOVIES, FullDataSet.R52,
				FullDataSet.RCV1, FullDataSet.WEBKB, FullDataSet.WIPO };
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
