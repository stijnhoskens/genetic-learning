package datasets;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FullDataSet extends AbstractDataSet {

	private static final String TEST = "test.txt";
	private static final String TRAIN = "train.txt";
	private static final String VOC = "voc.txt";
	private static final String VOC_FREQ = "voc_freq.txt";
	private static final String TEST_EXPLICIT = "test_explicit.txt";
	private static final String TRAIN_EXPLICIT = "train_explicit.txt";
	private static final String TOPICS = "topics.txt";

	public static final FullDataSet TWENTY_NG = new FullDataSet("datasets/20ng");
	public static final FullDataSet CLASSIC = new FullDataSet(
			"datasets/classic");
	public static final FullDataSet CORA = new FullDataSet("datasets/cora");
	public static final FullDataSet DMOZ = new FullDataSet("datasets/dmoz");
	public static final FullDataSet MOVIES = new FullDataSet("datasets/movies");
	public static final FullDataSet R52 = new FullDataSet("datasets/r52");
	public static final FullDataSet RCV1 = new FullDataSet("datasets/rcv1");
	public static final FullDataSet WEBKB = new FullDataSet("datasets/webkb");
	public static final FullDataSet WIPO = new FullDataSet("datasets/wipo");

	private final DataSet EVO_TEST;
	private final DataSet EVO_TRAIN;

	public FullDataSet(String directory) {
		super(directory);
		EVO_TEST = new DataSet(Paths.get(directory, "evoTest"), this);
		EVO_TRAIN = new DataSet(Paths.get(directory, "evoTrain"), this);
	}

	public FullDataSet(Path directory) {
		super(directory);
		EVO_TEST = new DataSet(directory.resolve("evoTest"), this);
		EVO_TRAIN = new DataSet(directory.resolve("evoTrain"), this);
	}

	public DataSet evoTest() {
		return EVO_TEST;
	}

	public DataSet evoTrain() {
		return EVO_TRAIN;
	}

	@Override
	public Path test() {
		return Paths.get(directory, TEST);
	}

	@Override
	public Path train() {
		return Paths.get(directory, TRAIN);
	}

	public Path voc() {
		return Paths.get(directory, VOC);
	}

	public Path vocFreq() {
		return Paths.get(directory, VOC_FREQ);
	}

	public Path topics() {
		return Paths.get(directory, TOPICS);
	}

	public Path testExplicit() {
		return Paths.get(directory, TEST_EXPLICIT);
	}

	public Path trainExplicit() {
		return Paths.get(directory, TRAIN_EXPLICIT);
	}

	public static Stream<FullDataSet> all() {
		return Stream.of(TWENTY_NG, CLASSIC, CORA, /* DMOZ, */MOVIES, R52,
				RCV1, WEBKB, WIPO);
	}
}
