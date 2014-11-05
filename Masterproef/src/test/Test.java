package test;

import java.util.stream.Stream;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibLINEAR;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import datasets.DataSet;

public class Test {

	public static void main(String[] args) throws Exception {
		DataSet data = DataSet.CLASSIC_TRAIN;
		DataSource src = new DataSource(data.train().toString());
		Instances train = src.getDataSet();
		train.setClassIndex(train.numAttributes() - 1);
		src = new DataSource(data.test().toString());
		Instances test = src.getDataSet();
		test.setClassIndex(test.numAttributes() - 1);
		AbstractClassifier clsfr = new LibLINEAR();
		Stream.of("0.0001", "0.001", "0.01", "0.1", "1", "10", "100", "1000")
				.forEach(s -> {
					try {
						clsfr.setOptions(new String[] { "-C", s });
						clsfr.buildClassifier(train);
						Evaluation eval = new Evaluation(train);
						eval.evaluateModel(clsfr, test);
						System.out.println(s);
						System.out.println(eval.pctCorrect());
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
	}
}
