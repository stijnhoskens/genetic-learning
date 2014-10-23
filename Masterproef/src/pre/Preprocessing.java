package pre;

import java.nio.file.Path;

import pre.data.DataSet;
import pre.data.DataSplitter;
import pre.parsers.CoraParser;
import pre.parsers.DataSetParser;
import pre.text.Vocabulary;
import pre.text.VocabularyFilter;
import pre.text.WordIndexer;

public class Preprocessing {

	public static void main(String[] args) throws Exception {
		// replace this with the desired dataset parser
		preprocess(new CoraParser());
	}

	public static void preprocess(DataSetParser parser) throws Exception {
		DataSet data = parser.getData();
		Path output = parser.parse(data.directory());
		System.out.println("parsing done");
		DataSplitter.split(output, data, 0.3d);
		System.out.println("splitting done");
		Vocabulary.buildAndExport(data);
		Vocabulary.WithFrequency.buildAndExport(data);
		System.out.println("building vocabulary done");
		VocabularyFilter.filter(data);
		System.out.println("filtering done");
		// ExplicitDataSorter.sort(data);
		// System.out.println("sorting done");
		WordIndexer.index(data);
		System.out.println("indexing done");
		System.out.println("preprocessing done");

	}
}
