package genetic.individuals.rules;

import java.nio.file.Path;
import java.util.function.Function;

import learning.Classifiers;
import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Action implements Function<Path, Classifier> {

	private final String clsfrName;

	public Action(String classifierName) {
		clsfrName = classifierName;
	}

	@Override
	public Classifier apply(Path train) {
		try {
			Instances instances = new DataSource(train.toString()).getDataSet();
			instances.setClassIndex(instances.numAttributes() - 1);
			Classifier classifier = Classifiers.get(clsfrName);
			classifier.buildClassifier(instances);
			return classifier;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
