package pre.data;

import io.IO;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.stream.Stream;

import datasets.FullDataSet;

public class DataSplitter {

	public static void splitIntoTrainTest(Path input, FullDataSet data,
			double testRatio) throws IOException {
		split(input, data.testExplicit(), data.trainExplicit(), testRatio);
	}

	public static void splitIntoEvoTrainTest(FullDataSet data, double ratio)
			throws IOException {
		split(data.test(), data.evoTest().testExplicit(), data.evoTrain()
				.testExplicit(), ratio);
		split(data.train(), data.evoTest().trainExplicit(), data.evoTrain()
				.trainExplicit(), ratio);
	}

	private static void split(Path input, Path test, Path train,
			double testRatio) throws IOException {
		if (!Files.exists(test))
			Files.createFile(test);
		if (!Files.exists(train))
			Files.createFile(train);
		Stream<String> lines = Files.lines(input);
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
		FullDataSet.ALL.forEach(data -> {
			try {
				DataSplitter.splitIntoEvoTrainTest(data, 0.5d);
				System.out.println("splitting of " + data + " done");
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

}
