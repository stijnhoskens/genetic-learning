package datasets.stats;

import io.IO;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

import datasets.DataSet;

public class Features {

	private final DataSet data;

	/*
	 * GENERAL FEATURES
	 */
	double nbOfDocs, nbOfTopics;

	/*
	 * DOCS-PER-TOPIC SPECIFIC
	 */
	double dptStd, dptEntr;

	/*
	 * WORDS-PER-DOC SPECIFIC
	 */
	double wpdAvg, wpdStd, wpdEntr;
	
	/*
	 * RELATED TO PCA
	 */
	double pca;

	Features(DataSet data) {
		this.data = data;
	}

	public DataSet getDataSet() {
		return data;
	}

	public double getNbOfDocs() {
		return nbOfDocs;
	}

	public double getNbOfTopics() {
		return nbOfTopics;
	}

	public double getDPTEntr() {
		return dptEntr;
	}

	public double getWPDAvg() {
		return wpdAvg;
	}

	public double getWPDEntr() {
		return wpdEntr;
	}
	
	public double getPCA() {
		return pca;
	}

	public double getEntry(int i) {
		return asArray()[i];
	}

	public static int nbOfFeatures() {
		return (int) fields().filter(f -> f.getType().equals(double.class))
				.count();
	}

	public double[] asArray() {
		return fields().filter(f -> {
			try {
				f.getDouble(this);
				return true;
			} catch (Exception e) {
				return false;
			}
		}).mapToDouble(f -> {
			try {
				return f.getDouble(this);
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
		}).toArray();
	}

	public static Features load(DataSet data) {
		Features stats = new Features(data);
		for (String l : IO.allLines(data.stats())) {
			try {
				String[] splitted = l.split(" ");
				String fieldName = splitted[0];
				Field field = Features.class.getDeclaredField(fieldName);
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
		IO.write(path, w -> fields().forEach(f -> {
			try {
				if (!f.getName().equals("data"))
					IO.writeLine(w, f.getName() + " " + f.get(this));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}));
	}

	private static Stream<Field> fields() {
		return Arrays.stream(Features.class.getDeclaredFields());
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

	}

}
