package parsers;

import java.nio.file.Path;
import java.util.stream.Stream;

import data.IO;
import models.ExplicitInstance;
import models.Instance;

public class InstancesParser {
	
	public static Stream<ExplicitInstance> parseExplicit(Path input) {
		return IO.lines(input).map(ExplicitInstance::new);
	}
	
	public static Stream<Instance> parse(Path input) {
		return IO.lines(input).map(Instance::new);
	}

}
