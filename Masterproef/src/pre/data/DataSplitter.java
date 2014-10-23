package pre.data;

import io.IO;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.stream.Stream;

import models.DataSet;

public class DataSplitter {

	public static void split(Path input, DataSet data, double testRatio)
			throws IOException {
		Stream<String> lines = Files.lines(input);
		Path test = data.testExplicit();
		Path train = data.trainExplicit();
		BufferedWriter testWriter = IO.writer(test);
		BufferedWriter trainWriter = IO.writer(train);
		Random r = new Random();
		lines.forEach(l -> {
			if (r.nextDouble() < testRatio)
				IO.writeLine(testWriter, l);
			else
				IO.writeLine(trainWriter, l);
		});
		lines.close();
		testWriter.close();
		trainWriter.close();
	}

	public static void main(String[] args) throws IOException {
		DataSplitter.split(DataSet.CORA.directory().resolve("cora.txt"),
				DataSet.CORA, 0.3d);
	}

}
