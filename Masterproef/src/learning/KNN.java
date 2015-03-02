package learning;

import java.time.Duration;
import java.time.LocalTime;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Evaluation;
import weka.classifiers.lazy.IBk;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import datasets.DataSet;

public class KNN extends AbstractClassifier {

	private static final long serialVersionUID = 1L;

	@Override
	public void buildClassifier(Instances data) throws Exception {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) throws Exception {
		IBk clsfr = new IBk();
		// weka.core.neighboursearch.LinearNNSearch
		// learning.DocumentNNSearch
		clsfr.setOptions(new String[] { "-K", "3", "-A",
				"learning.DocumentNNSearch" });
		DataSet data = DataSet.CLASSIC_TRAIN;
		Instances train = new DataSource(data.train().toString()).getDataSet();
		train.setClassIndex(train.numAttributes() - 1);
		LocalTime start = LocalTime.now();
		clsfr.buildClassifier(train);
		Instances test = new DataSource(data.test().toString()).getDataSet();
		test.setClassIndex(test.numAttributes() - 1);
		Evaluation eval = new Evaluation(train);
		eval.evaluateModel(clsfr, test);
		System.out.println(eval.pctCorrect());
		long elapsed = Duration.between(start, LocalTime.now()).getSeconds();
		System.out.println("Time needed: " + elapsed);
	}

}
