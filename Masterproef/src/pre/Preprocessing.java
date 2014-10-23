package pre;

import java.nio.file.Path;

import pre.data.DataSet;
import pre.data.DataSplitter;
import pre.data.ExplicitDataSorter;
import pre.parsers.CoraParser;
import pre.parsers.DataSetParser;
import pre.text.Vocabulary;
import pre.text.VocabularyFilter;
import pre.text.WordIndexer;

public class Preprocessing {

	public static void main(String[] args) throws Exception {
		// replace this with the desired dataset parser
		DataSetParser parser = new CoraParser();
		DataSet data = parser.getData();
		Path output = parser.parse(data.directory());
		DataSplitter.split(output, data, 0.3d);
		Vocabulary.buildAndExport(data);
		Vocabulary.WithFrequency.buildAndExport(data);
		VocabularyFilter.filter(data);
		ExplicitDataSorter.sort(data);
		WordIndexer.index(data);
	}
}
