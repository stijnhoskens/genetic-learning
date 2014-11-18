package learning;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.functions.LibLINEAR;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;

public class Classifiers {

	private static final Map<String, AbstractClassifier> CLASSIFIERS = new HashMap<String, AbstractClassifier>() {
		private static final long serialVersionUID = 1L;
		{
			put("SVM", new LibLINEAR());
			put("NaiveBayes", new NaiveBayesMultinomial());
			put("kNN", new IBk());
			put("DT", new J48());
		}
	};

	/**
	 * Returns a classifier specified by its name. The name can also include
	 * options like the -C parameter in SVM. These are all separated by a space.
	 * For instance a valid name is "SVM -C 10"
	 * 
	 * valid classifiers are:
	 * <ul>
	 * <li>SVM (LibLINEAR)</li>
	 * <li>NaiveBayes (NaiveBayesMultinomial)</li>
	 * <li>kNN (IBk)</li>
	 * <li>DT (J48)</li>
	 * </ul>
	 * The name between brackets specifies its corresponding classifier class.
	 * Look at the documentation to see its parameters.
	 */
	public static Classifier get(String name) throws Exception {
		String[] tokens = name.split(" ");
		String[] parameters = Arrays.copyOfRange(tokens, 1, tokens.length);
		AbstractClassifier clsfr = CLASSIFIERS.get(tokens[0]);
		clsfr.setOptions(parameters);
		return clsfr;
	}
}
