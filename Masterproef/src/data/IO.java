package data;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class IO {

	public static Stream<String> lines(Path path) throws IOException {
		return Files.lines(path);
	}

	public static List<String> allLines(Path path) throws IOException {
		return Files.readAllLines(path);
	}

	public static BufferedWriter writer(Path path) throws IOException {
		return Files.newBufferedWriter(path);
	}

	public static void write(Path path, Iterable<String> toWrite)
			throws IOException {
		Files.write(path, toWrite);
	}
	
	public static void writeLine(BufferedWriter writer, String line)
			throws IOException {
		writer.write(line);
		writer.newLine();
	}

}
