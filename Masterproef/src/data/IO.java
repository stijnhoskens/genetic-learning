package data;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class IO {

	public static Stream<String> lines(Path path) {
		try {
			return Files.lines(path);
		} catch (IOException e) {
			e.printStackTrace();
			return Stream.empty();
		}
	}

	public static List<String> allLines(Path path) {
		try {
			return Files.readAllLines(path);
		} catch (IOException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public static BufferedWriter writer(Path path) {
		try {
			return Files.newBufferedWriter(path);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @note has the addition of closing the writer after consuming it.
	 */
	public static void write(Path path, Consumer<BufferedWriter> consumer) {
		BufferedWriter writer;
		try {
			writer = writer(path);
			consumer.accept(writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static <T extends CharSequence> void write(Path path,
			Iterable<T> toWrite) {
		try {
			Files.write(path, toWrite);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeLine(BufferedWriter writer, String line) {
		try {
			writer.write(line);
			writer.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void replace(Path source, Path toReplace) {
		try {
			Files.copy(source, toReplace, StandardCopyOption.REPLACE_EXISTING);
			Files.delete(source);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
