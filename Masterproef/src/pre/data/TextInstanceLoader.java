package pre.data;

import io.IO;

import java.nio.file.Path;
import java.util.stream.Stream;

import pre.models.TextInstance;

public class TextInstanceLoader {

	/**
	 * @note Remember to close this stream, as it's basically a mapped IO
	 *       stream.
	 */
	public static Stream<TextInstance> load(Path path) {
		return IO.lines(path).map(TextInstance::new);
	}

	/**
	 * @note Remember to close this stream, as it's basically a mapped IO
	 *       stream.
	 */
	public static Stream<TextInstance> loadMultiple(Path... paths) {
		return IO.linesOfMultiple(paths).map(TextInstance::new);
	}

}
