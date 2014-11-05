package datasets.stats;

import io.IO;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

import datasets.DataSet;

public class DataSetFeatures {

	private final DataSet data;

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
	double dptMinMax;
	double dptVar;
	double dptStd;
	double dptSkew;
	double dptEntr;

	/*
	 * WORDS-PER-DOC SPECIFIC
	 */
	int wpdMax;
	int wpdMin;
	double wpdMinMax;
	double wpdAvg;
	double wpdVar;
	double wpdStd;
	double wpdSkew;
	double wpdEntr;

	public DataSetFeatures(DataSet data) {
		this.data = data;
	}

	public DataSet getDataSet() {
		return data;
	}

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

	public double getDPTMinMax() {
		return dptMinMax;
	}

	public double getDPTVar() {
		return dptVar;
	}

	public double getDPTEntr() {
		return dptEntr;
	}

	public int getWPDMax() {
		return wpdMax;
	}

	public int getWPDMin() {
		return wpdMin;
	}

	public double getWPDMinMax() {
		return wpdMinMax;
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

	public double getWPDEntr() {
		return wpdEntr;
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
		DataSetFeatures stats = new DataSetFeatures(data);
		for (String l : IO.allLines(data.stats())) {
			try {
				String[] splitted = l.split(" ");
				String fieldName = splitted[0];
				Field field = DataSetFeatures.class.getDeclaredField(fieldName);
				if (splitted[2].equals("int"))
					field.set(stats, Integer.parseInt(splitted[1]));
				else if (splitted[2].equals("double"))
					field.set(stats, Double.parseDouble(splitted[1]));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return stats;
	}

	public void export() throws IOException {
		Path path = data.stats();
		if (!Files.exists(path))
			Files.createFile(path);
		IO.write(
				path,
				w -> fields().forEach(
						f -> {
							try {
								if (!f.getName().equals("data"))
									IO.writeLine(w,
											f.getName() + " " + f.get(this)
													+ " " + f.getType());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}));
	}

	private Stream<Field> fields() {
		return Arrays.stream(getClass().getDeclaredFields());
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		fields().forEach(f -> {
			try {
				builder.append(f.getName() + " " + f.get(this) + "; ");
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		return builder.toString();
	}

	public static void main(String[] args) throws IOException {
		System.out.println(load(DataSet.CLASSIC_TRAIN));
	}

}
