package genetic.individuals.tree;

import learning.Classifiers;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import datasets.stats.DataSetFeatures;

public class Leaf implements DTNode {

	private final String name;

	public Leaf(String classifier) {
		this.name = classifier;
	}

	@Override
	public double accuracy(DataSetFeatures data) {
		try {
			Instances train = new DataSource(data.getDataSet().train()
					.toString()).getDataSet();
			train.setClassIndex(train.numAttributes() - 1);
			Instances test = new DataSource(data.getDataSet().test().toString())
					.getDataSet();
			test.setClassIndex(test.numAttributes() - 1);
			Classifier classifier = Classifiers.get(name);
			classifier.buildClassifier(train);
			Evaluation eval = new Evaluation(train);
			eval.evaluateModel(classifier, test);
			return eval.pctCorrect();
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

}
