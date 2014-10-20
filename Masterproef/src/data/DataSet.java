package data;

import java.nio.file.Path;
import java.nio.file.Paths;

public class DataSet {

	private static final String TEST = "test.txt";
	private static final String TRAIN = "train.txt";
	private static final String VOC = "voc.txt";
	private static final String VOC_FREQ = "voc_freq.txt";

	public static final DataSet TWENTY_NG = new DataSet("datasets/20ng");
	public static final DataSet CLASSIC = new DataSet("datasets/classic");
	public static final DataSet DMOZ = new DataSet("datasets/dmoz");
	public static final DataSet MOVIES = new DataSet("datasets/movies");
	public static final DataSet R52 = new DataSet("datasets/r52");
	public static final DataSet RCV1 = new DataSet("datasets/rcv1");
	public static final DataSet WEBKB = new DataSet("datasets/webkb");
	public static final DataSet WIPO = new DataSet("datasets/wipo");

	private String directory;

	public DataSet(String directory) {
		this.directory = directory;
	}

	public Path directory() {
		return Paths.get(directory);
	}

	public Path test() {
		return Paths.get(directory, TEST);
	}

	public Path train() {
		return Paths.get(directory, TRAIN);
	}

	public Path voc() {
		return Paths.get(directory, VOC);
	}

	public Path vocFreq() {
		return Paths.get(directory, VOC_FREQ);
	}
}
