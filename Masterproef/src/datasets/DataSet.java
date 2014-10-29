package datasets;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DataSet extends AbstractDataSet {

	public static final DataSet TWENTY_NG_TRAIN = FullDataSet.TWENTY_NG
			.evoTrain();
	public static final DataSet TWENTY_NG_TEST = FullDataSet.TWENTY_NG
			.evoTest();
	public static final DataSet CLASSIC_TRAIN = FullDataSet.CLASSIC.evoTrain();
	public static final DataSet CLASSIC_TEST = FullDataSet.CLASSIC.evoTest();
	public static final DataSet CORA_TRAIN = FullDataSet.CORA.evoTrain();
	public static final DataSet CORA_TEST = FullDataSet.CORA.evoTest();
	public static final DataSet DMOZ_TRAIN = FullDataSet.DMOZ.evoTrain();
	public static final DataSet DMOZ_TEST = FullDataSet.DMOZ.evoTest();
	public static final DataSet MOVIES_TRAIN = FullDataSet.MOVIES.evoTrain();
	public static final DataSet MOVIES_TEST = FullDataSet.MOVIES.evoTest();
	public static final DataSet R52_TRAIN = FullDataSet.R52.evoTrain();
	public static final DataSet R52_TEST = FullDataSet.R52.evoTest();
	public static final DataSet RCV1_TRAIN = FullDataSet.RCV1.evoTrain();
	public static final DataSet RCV1_TEST = FullDataSet.RCV1.evoTest();
	public static final DataSet WEBKB_TRAIN = FullDataSet.WEBKB.evoTrain();
	public static final DataSet WEBKB_TEST = FullDataSet.WEBKB.evoTest();
	public static final DataSet WIPO_TRAIN = FullDataSet.WIPO.evoTrain();
	public static final DataSet WIPO_TEST = FullDataSet.WIPO.evoTest();

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
