package data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import models.ExplicitInstance;

public class ExplicitDataSorter {

	public static void sort(DataSet data) throws IOException {
		sort(data.testExplicit());
		sort(data.trainExplicit());
	}

	public static void sort(Path path) throws IOException {
		Path temp = Files.createTempFile(path.getParent(), null, null);
		Stream<String> lines = IO.lines(path);
		IO.write(temp, w -> {
			lines.forEach(l -> IO.writeLine(w,
					new ExplicitInstance(l, false).toString()));
		});
		lines.close();
		IO.replace(temp, path);
	}

	public static void main(String[] args) throws IOException {
		ExplicitDataSorter.sort(DataSet.R52);
	}

}
