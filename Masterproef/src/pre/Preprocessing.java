package pre;

import java.nio.file.Path;

import datasets.DataSet;
import datasets.FullDataSet;
import pre.data.DataSplitter;
import pre.data.ToArffConverter;
import pre.data.TopicsCollector;
import pre.models.Vocabulary;
import pre.parsers.CoraParser;
import pre.parsers.DataSetParser;
import pre.text.VocabularyFilter;
import pre.text.WordIndexer;

public class Preprocessing {

	public static void main(String[] args) throws Exception {
		// replace this with the desired dataset parser
		preprocess(new CoraParser());
	}

	public static void preprocess(DataSetParser parser) throws Exception {
		FullDataSet data = parser.getData();
		Path output = parser.parse(data.directory());
		System.out.println("parsing done");
		DataSplitter.splitIntoTrainTest(output, data, 0.3d);
		System.out.println("splitting into learning-specific split done");
		Vocabulary.buildAndExport(data);
		Vocabulary.WithFrequency.buildAndExport(data);
		System.out.println("building vocabulary done");
		VocabularyFilter.filter(data);
		System.out.println("filtering done");
		// ExplicitDataSorter.sort(data);
		// System.out.println("sorting done");
		WordIndexer.index(data);
		System.out.println("indexing done");
		DataSplitter.splitIntoEvoTrainTest(data, 0.5d);
		System.out.println("splitting into evolution-specific split done");
		DataSet evoTest = data.evoTest();
		ToArffConverter.convert(evoTest.testExplicit(), evoTest.test(),
				Vocabulary.load(data), TopicsCollector.load(data));
		DataSet evoTrain = data.evoTrain();
		ToArffConverter.convert(evoTrain.testExplicit(), evoTrain.test(),
				Vocabulary.load(data), TopicsCollector.load(data));
		System.out.println("conversion to ARFF done");
		System.out.println("preprocessing done");

	}
}
