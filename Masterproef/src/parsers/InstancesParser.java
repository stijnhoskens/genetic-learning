package parsers;

import java.nio.file.Path;
import java.util.stream.Stream;

import data.IO;
import models.ExplicitInstance;

public class InstancesParser {
	
	public static Stream<ExplicitInstance> parse(Path input) {
		return IO.lines(input).map(ExplicitInstance::new);
	}

}
