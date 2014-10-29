package datasets.stats;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;

import pre.models.WordFrequencyPair;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class TopicDistribution {

	private final List<WordFrequencyPair> wfps = new ArrayList<>();

	public TopicDistribution(List<String> topics, int[] distribution) {
		for (int i = 0; i < topics.size(); i++)
			wfps.add(new WordFrequencyPair(topics.get(i), distribution[i]));
	}

	public List<WordFrequencyPair> asWordFreqPairs() {
		return Collections.unmodifiableList(wfps);
	}

	public List<WordFrequencyPair> asSortedWordFreqPairs() {
		List<WordFrequencyPair> sorted = new ArrayList<>(wfps);
		sorted.sort(comparingFreq().reversed());
		return Collections.unmodifiableList(sorted);
	}

	public IntSummaryStatistics frequencyStats() {
		return wfps.stream().mapToInt(WordFrequencyPair::getFrequency)
				.summaryStatistics();
	}

	private Comparator<WordFrequencyPair> comparingFreq() {
		return Comparator.comparingInt(WordFrequencyPair::getFrequency);
	}

	public static TopicDistribution calculate(Path path, List<String> topics)
			throws Exception {
		DataSource src = new DataSource(path.toString());
		Instances instances = src.getDataSet();
		instances.setClassIndex(instances.numAttributes() - 1);
		int[] distribution = new int[topics.size()];
		for (int i = 0; i < instances.numInstances(); i++) {
			Instance instance = instances.instance(i);
			String topic = instance.toString(instance.classAttribute());
			distribution[topics.indexOf(topic)]++;
		}
		return new TopicDistribution(topics, distribution);
	}

}
