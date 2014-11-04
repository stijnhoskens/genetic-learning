package datasets.stats;

import java.nio.file.Path;
import java.util.IntSummaryStatistics;
import java.util.List;

import pre.data.TopicsCollector;
import pre.models.Vocabulary;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import datasets.DataSet;

public class StatisticsExtractor {

	public static DataSetFeatures extract(DataSet data) throws Exception {
		DataSetFeatures features = new DataSetFeatures();
		List<String> topics = TopicsCollector.load(data.fullDataSet());
		features.nbOfTopics = topics.size();
		features.vocSize = Vocabulary.sizeWithoutLoading(data.fullDataSet());
		IntDistribution topicDistribution = getTopicDistribution(data.train());
		IntSummaryStatistics stats = topicDistribution.frequencyStats();
		features.dptMax = stats.getMax();
		features.dptMin = stats.getMin();
		features.nbOfDocs = (int) stats.getSum();
		return features;
	}

	private static IntDistribution getTopicDistribution(Path path)
			throws Exception {
		Instances instances = new DataSource(path.toString()).getDataSet();
		return new IntDistribution(instances.attributeStats(instances
				.numAttributes() - 1).nominalCounts);
	}
}
