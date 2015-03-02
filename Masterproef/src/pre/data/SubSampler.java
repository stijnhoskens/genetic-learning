package pre.data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.supervised.instance.Resample;
import datasets.DataSet;

public class SubSampler {

	public static final int MAX_TRAIN_SIZE = 11900;
	public static final int MAX_TEST_SIZE = 5100;

	public static void subsample(DataSet data) {
		Instances train = data.trainInstances();
		if (train.size() < MAX_TRAIN_SIZE)
			// nothing to do here
			return;
		try {
			subsample(train, data.train(), data.fullTrain(), MAX_TRAIN_SIZE);
			subsample(data.testInstances(), data.test(), data.fullTest(),
					MAX_TEST_SIZE);
		} catch (IOException e) {
			System.out.println("Something I/O related went wrong.");
		}

	}

	public static void subsample(Instances instances, Path input, Path backup,
			int size) throws IOException {
		Files.copy(input, backup, StandardCopyOption.REPLACE_EXISTING);
		Instances newInstances = reduce(instances, size);
		System.out.println(newInstances.size());
		ArffSaver output = new ArffSaver();
		output.setInstances(newInstances);
		output.setFile(input.toFile());
		output.writeBatch();
	}

	public static Instances reduce(Instances instances, int size) {
		int nbOfInstances = instances.size();
		double percentage = 100d * ((double) size) / ((double) nbOfInstances);
		System.out.println(percentage);
		Resample filter = new Resample();
		try {
			filter.setSampleSizePercent(percentage);
			filter.setInputFormat(instances);
			return Filter.useFilter(instances, filter);
		} catch (Exception e) {
			e.printStackTrace();
			return instances;
		}
	}

	public static void main(String[] args) {
		subsample(DataSet.WIPO_TEST);
	}

}
