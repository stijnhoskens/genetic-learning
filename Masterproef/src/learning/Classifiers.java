package learning;

import genetic.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import util.Joiner;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;

public class Classifiers {

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
		AbstractClassifier clsfr = getClassifier(tokens[0]);
		clsfr.setOptions(parameters);
		return clsfr;
	}

	private static AbstractClassifier getClassifier(String name) {
		try {
			return (AbstractClassifier) Class.forName(
					Config.CLASSIFIERS.getProperty(name).split(" ")[0])
					.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Set<String> all() {
		return Config.CLASSIFIERS.stringPropertyNames();
	}

	public static String randomClassifier() {
		List<String> list = new ArrayList<>(all());
		Random r = new Random();
		String name = list.get(r.nextInt(list.size()));
		String parameters = Joiner.join(
				getParametersOf(name).stream().map(
						p -> p.toString(r.nextInt(p.nbOfValues()))), " ");
		return name + " " + parameters;

	}

	public static Set<Parameter> getParametersOf(String name) {
		String[] splitted = Config.CLASSIFIERS.getProperty(name).split(" ");
		String[] parameters = Arrays.copyOfRange(splitted, 1, splitted.length);
		return Arrays.stream(parameters).map(Parameter::new)
				.collect(Collectors.toSet());
	}

	public static void main(String[] args) throws Exception {
		System.out.println(get(randomClassifier()).getClass());
	}
}
