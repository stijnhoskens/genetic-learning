package data;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.Stream;

public class DataSplitter {

	public final Stream<String> lines;

	public DataSplitter(Path input) throws IOException {
		lines = Files.lines(input);
	}

	public void split(Path test, Path train, double testRatio)
			throws IOException {
		BufferedWriter testWriter = IO.writer(test);
		BufferedWriter trainWriter = IO.writer(train);
		Random r = new Random();
		lines.forEach(l -> {
			try {
				if (r.nextDouble() < testRatio)
					IO.writeLine(testWriter, l);
				else
					IO.writeLine(trainWriter, l);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public static void main(String[] args) throws IOException {
		new DataSplitter(Paths.get("datasets/rcv1/rcv1.txt")).split(
				DataPath.RCV1_TEST, DataPath.RCV1_TRAIN, 0.3d);
	}

}
