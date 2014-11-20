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
	int nbOfDocs, nbOfTopics;

	/*
	 * DOCS-PER-TOPIC SPECIFIC
	 */
	double dptStd, dptEntr;

	/*
	 * WORDS-PER-DOC SPECIFIC
	 */
	double wpdAvg, wpdStd, wpdEntr;

	Features(DataSet data) {
		this.data = data;
	}

	public DataSet getDataSet() {
		return data;
	}

	public int getNbOfDocs() {
		return nbOfDocs;
	}

	public int getNbOfTopics() {
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

	public double getEntry(int i) {
		return asArray()[i];
	}

	public int nbOfFeatures() {
		return asArray().length;
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
		System.out.println(Arrays.toString(load(DataSet.CLASSIC_TRAIN)
				.asArray()));
	}

}
