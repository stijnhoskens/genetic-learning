package test;

import weka.classifiers.Classifier;
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
		Classifier clsfr = new LibLINEAR();		
		clsfr.buildClassifier(train);
		Evaluation eval = new Evaluation(train);
		eval.evaluateModel(clsfr, test);
		System.out.println(eval.toSummaryString("\nResults\n======\n", false));
	}
}
