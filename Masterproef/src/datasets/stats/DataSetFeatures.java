package datasets.stats;

import io.IO;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

import datasets.DataSet;

public class DataSetFeatures {

	/*
	 * GENERAL FEATURES
	 */
	int nbOfDocs;
	int vocSize;
	int nbOfTopics;

	/*
	 * DOCS-PER-TOPIC SPECIFIC
	 */
	int dptMax;
	int dptMin;
	double dptVar;
	double dptSkew;

	/*
	 * WORDS-PER-DOC SPECIFIC
	 */
	int wpdMax;
	int wpdMin;
	double wpdAvg;
	double wpdVar;
	double wpdSkew;

	public int getNbOfDocs() {
		return nbOfDocs;
	}

	public int getVocSize() {
		return vocSize;
	}

	public int getNbOfTopics() {
		return nbOfTopics;
	}

	public int getDPTMax() {
		return dptMax;
	}

	public double getDPTSkew() {
		return dptSkew;
	}

	public int getDPTMin() {
		return dptMin;
	}

	public double getDPTVar() {
		return dptVar;
	}

	public int getWPDMax() {
		return wpdMax;
	}

	public int getWPDMin() {
		return wpdMin;
	}

	public double getWPDAvg() {
		return wpdAvg;
	}

	public double getWPDVar() {
		return wpdVar;
	}

	public double getWPDSkew() {
		return wpdSkew;
	}

	public double[] asArray() {
		return fields().mapToDouble(f -> {
			try {
				return f.getDouble(this);
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
		}).toArray();
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
				if (splitted[2].equals("int"))
					method.invoke(stats, Integer.parseInt(splitted[1]));
				else if (splitted[2].equals("double"))
					method.invoke(stats, Double.parseDouble(splitted[1]));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return stats;
	}

	public void export(DataSet data) throws IOException {
		Path path = data.stats();
		if (!Files.exists(path))
			Files.createFile(path);
		IO.write(
				path,
				w -> fields().forEach(
						f -> {
							try {
								IO.writeLine(w, f.getName() + " " + f.get(this)
										+ " " + f.getType());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}));
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

	private Stream<Field> fields() {
		return Arrays.stream(getClass().getDeclaredFields());
	}

	public static void main(String[] args) throws IOException {
		new DataSetFeatures().export(DataSet.CLASSIC_TRAIN);
	}

}
