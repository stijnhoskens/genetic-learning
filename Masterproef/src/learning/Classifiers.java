package learning;

import genetic.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import util.Joiner;
import weka.classifiers.AbstractClassifier;
import weka.classifiers.Classifier;
import weka.core.Instances;

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

	public static Classifier trained(String name, Instances train)
			throws Exception {
		Classifier c = get(name);
		c.buildClassifier(train);
		return c;
	}

	private static AbstractClassifier getClassifier(String name) {
		try {
			String[] tokens = Config.CLASSIFIERS.getProperty(name).split(" ");
			AbstractClassifier clsfr = (AbstractClassifier) Class.forName(
					tokens[0]).newInstance();
			String[] fixedOptions = Arrays.stream(tokens)
					.filter(s -> s.startsWith("(FIXED)"))
					.map(s -> s.replace("(FIXED)", "")).map(Parameter::new)
					.map(p -> p.toString(0)).map(s -> s.split(" "))
					.flatMap(Arrays::stream).toArray(String[]::new);
			clsfr.setOptions(fixedOptions);
			return clsfr;
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

	public static List<Parameter> getParametersOf(String name) {
		String[] splitted = Config.CLASSIFIERS.getProperty(name).split(" ");
		String[] parameters = Arrays
				.stream(Arrays.copyOfRange(splitted, 1, splitted.length))
				.filter(s -> !s.startsWith("(FIXED)")).toArray(String[]::new);
		return Arrays.stream(parameters).map(Parameter::new)
				.sorted((p1, p2) -> p1.toString().compareTo(p2.toString()))
				.collect(Collectors.toList());
	}

	public static Stream<String> allOptions() {
		return all().stream().flatMap(Classifiers::allOptionsOf);
	}

	private static Stream<String> allOptionsOf(String c) {
		List<Parameter> parameters = getParametersOf(c);
		if (parameters.isEmpty())
			return Stream.of(c);
		return parameters
				.stream()
				.map(Parameter::possibleValues)
				.reduce(Stream.of("").collect(Collectors.toSet()),
						(s1, s2) -> {
							Builder<String> builder = Stream.builder();
							s1.forEach(string1 -> s2.forEach(string2 -> builder
									.accept(Joiner.join(" ", string1, string2))));
							return builder.build().collect(Collectors.toSet());
						}).stream().map(s -> c + s);
	}

	public static void main(String[] args) throws Exception {
		System.out.println(allOptions().count());
	}
}
