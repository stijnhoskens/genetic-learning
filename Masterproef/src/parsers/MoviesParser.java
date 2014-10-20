package parsers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import text.SentenceProcessing;
import data.DataSet;
import data.IO;

public class MoviesParser implements DataSetParser {

	public void parse(Path directory) throws IOException {
		Path temp = Files.createFile(directory.resolve("instances.txt"));
		IO.write(temp, w -> {
			IO.filesIn(directory)
			.filter(p -> !p.equals(temp))
			.forEach(p -> {
				IO.filesIn(p).forEach(f ->  { 
					String text = IO.lines(f).collect(StringBuilder::new, (sb, s) -> 
					sb.append(s), (a0, a1) -> a0.append(a1)).toString();
					String processed = SentenceProcessing.toProcessedString(text);
					IO.writeLine(w, p.getFileName().toString().toUpperCase() + 
						" " + processed);});
			});

		});
	}

	public static void main(String[] args) throws IOException {
		new MoviesParser().parse(DataSet.MOVIES.directory());
	}

}
