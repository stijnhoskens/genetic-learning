package datasets.stats;

import io.IO;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import datasets.DataSet;

public class DataSetFeatures {

	private int nbOfDocs;
	private int nbOfWords;
	private int nbOfTopics;
	private int maxDocsPerTopic;
	private int minDocsPerTopic;

	private double skewness;
	private double entropy;
	private double variance;

	DataSetFeatures() {

	}

	public int getNbOfDocs() {
		return nbOfDocs;
	}

	void setNbOfDocs(int nbOfDocs) {
		this.nbOfDocs = nbOfDocs;
	}

	public int getNbOfWords() {
		return nbOfWords;
	}

	void setNbOfWords(int nbOfWords) {
		this.nbOfWords = nbOfWords;
	}

	public int getNbOfTopics() {
		return nbOfTopics;
	}

	void setNbOfTopics(int nbOfTopics) {
		this.nbOfTopics = nbOfTopics;
	}

	public int getMaxDocsPerTopic() {
		return maxDocsPerTopic;
	}

	void setMaxDocsPerTopic(int maxDocsPerTopic) {
		this.maxDocsPerTopic = maxDocsPerTopic;
	}

	public int getMinDocsPerTopic() {
		return minDocsPerTopic;
	}

	void setMinDocsPerTopic(int minDocsPerTopic) {
		this.minDocsPerTopic = minDocsPerTopic;
	}

	public double getSkewness() {
		return skewness;
	}

	void setSkewness(double skewness) {
		this.skewness = skewness;
	}

	public double getEntropy() {
		return entropy;
	}

	void setEntropy(double entropy) {
		this.entropy = entropy;
	}

	public double getVariance() {
		return variance;
	}

	void setVariance(double variance) {
		this.variance = variance;
	}

	public static DataSetFeatures load(DataSet data) {
		DataSetFeatures stats = new DataSetFeatures();
		for (String l : IO.allLines(data.stats())) {
			try {
				String[] splitted = l.split(" ");
				String fieldName = splitted[0];
				char first = Character.toUpperCase(fieldName.charAt(0));
				String methodName = "set" + first + splitted[0].substring(1);
				Method method = DataSetFeatures.class.getDeclaredMethod(
						methodName, getClass(splitted[2]));
				if (getClass(splitted[2]).equals(int.class))
					method.invoke(stats, Integer.parseInt(splitted[1]));
				else if (getClass(splitted[2]).equals(double.class))
					method.invoke(stats, Double.parseDouble(splitted[1]));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return stats;
	}

	private static Class<?> getClass(String prim) {
		switch (prim) {
		case "int":
			return int.class;
		case "double":
			return double.class;
		default:
			return Object.class;
		}
	}

	public void export(DataSet data) throws IOException {
		Path path = data.stats();
		if (!Files.exists(path))
			Files.createFile(path);
		IO.write(
				path,
				w -> Arrays.stream(this.getClass().getDeclaredFields())
						.forEach(
								f -> {
									try {
										IO.writeLine(w,
												f.getName() + " " + f.get(this)
														+ " " + f.getType());
									} catch (Exception e) {
										e.printStackTrace();
									}
								}));
	}

}
