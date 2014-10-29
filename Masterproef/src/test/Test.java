package test;

import datasets.FullDataSet;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Test {

	public static void main(String[] args) throws Exception {

		DataSource src = new DataSource(FullDataSet.TWENTY_NG.evoTest().test()
				.toString());
		Instances data = src.getDataSet();
		if (data.classIndex() == -1)
			data.setClassIndex(data.numAttributes() - 1);
		System.out.println(data.firstInstance().toString());

	}

}
