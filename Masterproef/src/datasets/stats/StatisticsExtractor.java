package datasets.stats;

import java.util.IntSummaryStatistics;
import java.util.List;

import pre.data.TopicsCollector;
import pre.models.Vocabulary;
import datasets.DataSet;

public class StatisticsExtractor {

	public static DataSetFeatures extract(DataSet data) throws Exception {
		DataSetFeatures features = new DataSetFeatures();
		List<String> topics = TopicsCollector.load(data.fullDataSet());
		features.setNbOfTopics(topics.size());
		features.setNbOfWords(Vocabulary.sizeWithoutLoading(data.fullDataSet()));
		TopicDistribution distr = TopicDistribution.calculate(data.train(),
				topics);
		IntSummaryStatistics stats = distr.frequencyStats();
		features.setMaxDocsPerTopic(stats.getMax());
		features.setMinDocsPerTopic(stats.getMin());
		features.setNbOfDocs((int) stats.getSum());
		return features;
	}

	public static void main(String[] args) throws Exception {
		extract(DataSet.CLASSIC_TEST).export(DataSet.CLASSIC_TEST);
	}
}
