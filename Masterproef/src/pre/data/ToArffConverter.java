package pre.data;

import io.IO;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import pre.models.IndexFrequencyPair;
import pre.models.TextInstance;
import pre.models.Vocabulary;
import datasets.FullDataSet;

public class ToArffConverter {

	private static final String RELATION_TAG = "@RELATION";
	private static final String ATTRIBUTE_TAG = "@ATTRIBUTE";
	private static final String DATA_TAG = "@DATA";

	/**
	 * @param in
	 *            the path to the test or train.txt file
	 * @param out
	 *            the path where your ARFF file should be saved
	 * @param voc
	 *            the vocabulary corresponding to the input file
	 * @param topics
	 *            contains all topics as strings
	 */
	public static void convert(Path in, Path out, Vocabulary voc,
			List<String> topics) {
		IO.write(out, w -> {
			String relationName = in.toString();
			IO.writeLine(w, RELATION_TAG + " " + relationName);
			IO.newLine(w);
			for (String word : voc.listOfTerms()) {
				if (word.contains("'"))
					word = "'" + word.replaceAll("'", "\\\\'") + "'";
				if (word.contains("\""))
					word = "'" + word.replaceAll("\"", "\\\\\"") + "'";
				IO.writeLine(w, ATTRIBUTE_TAG + " " + word + " NUMERIC");
			}
			String topicsString = "{";
			for (String topic : topics)
				topicsString += topic + ",";
			topicsString = topicsString.substring(0, topicsString.length() - 1)
					.concat("}");
			IO.writeLine(w, ATTRIBUTE_TAG + " TOPIC " + topicsString);
			IO.newLine(w);
			IO.writeLine(w, DATA_TAG);
			Stream<TextInstance> instances = TextInstanceLoader.load(in);
			instances.forEach(i -> {
				StringBuilder buffer = new StringBuilder("{");
				for (IndexFrequencyPair pair : i.getWords())
					buffer.append(pair.getIndex() + " " + pair.getFrequency()
							+ ",");
				buffer.append(voc.size() + " " + i.getTopic() + "}");
				IO.writeLine(w, buffer.toString());
			});
			instances.close();
		});
	}

	public static void convert(FullDataSet full) {
		Vocabulary voc = Vocabulary.load(full);
		List<String> topics = TopicsCollector.load(full);
		Stream.of(full.evoTest(), full.evoTrain()).forEach(
				data -> {
					ToArffConverter.convert(data.testExplicit(), data.test(),
							voc, topics);
					ToArffConverter.convert(data.trainExplicit(), data.train(),
							voc, topics);
				});
	}

	public static void main(String[] args) {
		FullDataSet.all().forEach(
				full -> {
					if (FullDataSet.RCV1.equals(full)) {
						convert(full);
						System.out.println("conversion of " + full.toString()
								+ " done");
					}
				});
	}
}
