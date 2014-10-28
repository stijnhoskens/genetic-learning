package pre.models;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DataSet extends AbstractDataSet {

	private static final String TEST = "test.arff";
	private static final String TRAIN = "train.arff";
	private static final String TEST_EXPLICIT = "test_explicit.txt";
	private static final String TRAIN_EXPLICIT = "train_explicit.txt";

	public DataSet(String directory) {
		super(directory);
	}

	public DataSet(Path directory) {
		super(directory);
	}

	@Override
	public Path test() {
		return Paths.get(directory, TEST);
	}

	@Override
	public Path train() {
		return Paths.get(directory, TRAIN);
	}

	public Path testExplicit() {
		return Paths.get(directory, TEST_EXPLICIT);
	}

	public Path trainExplicit() {
		return Paths.get(directory, TRAIN_EXPLICIT);
	}

}
