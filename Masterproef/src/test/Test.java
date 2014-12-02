package test;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibLINEAR;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import datasets.DataSet;

public class Test {

	public static void main(String[] args) throws Exception {
		DataSet data = DataSet.WIPO_TRAIN;
		DataSource src = new DataSource(data.train().toString());
		Instances train = src.getDataSet();
		train.setClassIndex(train.numAttributes() - 1);
		src = new DataSource(data.test().toString());
		Instances test = src.getDataSet();
		test.setClassIndex(test.numAttributes() - 1);
		AbstractClassifier clsfr = new LibLINEAR();
		long start = System.currentTimeMillis();
		System.out.println("Started building the classifier.");
		clsfr.buildClassifier(train);
		long elapsed = (System.currentTimeMillis() - start) / 1000;
		System.out.println("Building done, elapsed time: " + elapsed + "s");
		start = System.currentTimeMillis();
		System.out.println("Started evaluating the classifier.");
		Evaluation eval = new Evaluation(train);
		eval.evaluateModel(clsfr, test);
		elapsed = (System.currentTimeMillis() - start) / 1000;
		System.out.println("Evaluation done, elapsed time: " + elapsed + "s");
		System.out.println(eval.pctCorrect());
	}
}
