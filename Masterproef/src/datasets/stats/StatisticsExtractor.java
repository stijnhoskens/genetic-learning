package datasets.stats;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;

import pre.data.TopicsCollector;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import datasets.DataSet;

public class StatisticsExtractor {

	public static Features extract(DataSet data) throws Exception {
		Features features = new Features(data);
		List<String> topics = TopicsCollector.load(data.fullDataSet());
		features.nbOfTopics = topics.size();
		Path train = data.train();
		IntDistribution topicDistribution = getTopicDistribution(train);
		IntSummaryStatistics stats = topicDistribution.frequencyStats();
		features.nbOfDocs = stats.getSum();
		features.dptStd = topicDistribution.standardDeviation();
		features.dptEntr = topicDistribution.entropy();
		IntDistribution docDistribution = getDocDistribution(train);
		stats = docDistribution.frequencyStats();
		features.wpdAvg = stats.getAverage();
		features.wpdStd = docDistribution.standardDeviation();
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
		DataSet.all().forEach(
				data -> {
					try {
						if (data.equals(DataSet.DMOZ_TEST)
								|| data.equals(DataSet.DMOZ_TRAIN))
							return;
						extract(data).export();
						System.out.print(data);
						System.out.println(" exported.");
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
	}
}
