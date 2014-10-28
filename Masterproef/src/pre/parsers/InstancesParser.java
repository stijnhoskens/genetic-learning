package pre.parsers;

import io.IO;

import java.nio.file.Path;
import java.util.stream.Stream;

import pre.models.ExplicitInstance;
import pre.models.TextInstance;

public class InstancesParser {
	
	public static Stream<ExplicitInstance> parseExplicit(Path input) {
		return IO.lines(input).map(ExplicitInstance::new);
	}
	
	public static Stream<TextInstance> parse(Path input) {
		return IO.lines(input).map(TextInstance::new);
	}

}
