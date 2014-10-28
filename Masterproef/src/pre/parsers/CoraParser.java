package pre.parsers;

import io.IO;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import pre.models.FullDataSet;
import pre.text.SentenceProcessing;

public class CoraParser implements DataSetParser {

	@Override
	public Path parse(Path directory) throws Exception {
		Path data = directory.resolve("data");
		Path classes = data.resolve("classifications");
		Path parsed = directory.resolve("cora.txt");
		if (!Files.exists(parsed))
			Files.createFile(parsed);
		IO.write(parsed, w -> IO.lines(classes).forEach(l -> {
			String[] splitted = l.split("\t");
			if (splitted.length < 2 || splitted[0].equals("keywords"))
				return;
			String fileName = splitted[0].replace(':', '_');
			if (!fileName.endsWith(".ps"))
				return;
			Path file = data.resolve(fileName);
			if (!Files.exists(file))
				return;
			Optional<String> opt = getAbstract(file);
			if (!opt.isPresent())
				return;
			String abstr = SentenceProcessing.toProcessedString(opt.get());
			if (abstr.equals(""))
				return;
			IO.writeLine(w, splitted[1] + " " + abstr);
		}));
		return parsed;
	}

	public Optional<String> getAbstract(Path file) {
		Optional<String> opt = IO.lines(file)
				.filter(l -> l.startsWith("Abstract:")).findAny();
		if (!opt.isPresent())
			return Optional.empty();
		String line = opt.get().replace("Abstract:", "");
		return Optional.of(line);
	}

	public static void main(String[] args) throws Exception {
		new CoraParser().parse(FullDataSet.CORA.directory());
	}

	@Override
	public FullDataSet getData() {
		return FullDataSet.CORA;
	}

}
