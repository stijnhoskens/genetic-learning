package datasets.stats;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;

import pre.data.TopicsCollector;
import pre.models.Vocabulary;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import datasets.DataSet;

public class StatisticsExtractor {

	public static DataSetFeatures extract(DataSet data) throws Exception {
		DataSetFeatures features = new DataSetFeatures(data);
		List<String> topics = TopicsCollector.load(data.fullDataSet());
		features.nbOfTopics = topics.size();
		features.vocSize = Vocabulary.sizeWithoutLoading(data.fullDataSet());
		Path train = data.train();
		IntDistribution topicDistribution = getTopicDistribution(train);
		IntSummaryStatistics stats = topicDistribution.frequencyStats();
		features.dptMax = stats.getMax();
		features.dptMin = stats.getMin();
		features.dptMinMax = (double) features.dptMin
				/ (double) features.dptMax;
		features.nbOfDocs = (int) stats.getSum();
		features.dptVar = topicDistribution.variance();
		features.dptStd = Math.sqrt(features.dptVar);
		features.dptSkew = topicDistribution.skewness();
		features.dptEntr = topicDistribution.entropy();
		IntDistribution docDistribution = getDocDistribution(train);
		stats = docDistribution.frequencyStats();
		features.wpdAvg = stats.getAverage();
		features.wpdMax = stats.getMax();
		features.wpdMin = stats.getMin();
		features.wpdMinMax = (double) features.wpdMin
				/ (double) features.wpdMax;
		features.wpdVar = docDistribution.variance();
		features.wpdStd = Math.sqrt(features.wpdVar);
		features.wpdSkew = docDistribution.skewness();
		features.wpdEntr = docDistribution.entropy();
		return features;
	}

	/**
	 * Returns a distribution of how many documents categorize in a certain
	 * topic.
	 */
	private static IntDistribution getTopicDistribution(Path path)
			throws Exception {
		Instances instances = new DataSource(path.toString()).getDataSet();
		return new IntDistribution(instances.attributeStats(instances
				.numAttributes() - 1).nominalCounts);
	}

	/**
	 * Returns a distribution of how many words are contained in a certain
	 * document.
	 */
	private static IntDistribution getDocDistribution(Path path)
			throws Exception {
		Instances instances = new DataSource(path.toString()).getDataSet();
		return new IntDistribution(instances.stream().mapToInt(i -> {
			double[] doubles = i.toDoubleArray();
			return (int) Arrays.stream(doubles, 0, doubles.length - 1).sum();
		}).toArray());
	}

	public static void main(String[] args) throws Exception {
		DataSet.ALL.forEach(data -> {
			try {
				extract(data).export();
				System.out.print(data);
				System.out.println(" exported.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}
}
