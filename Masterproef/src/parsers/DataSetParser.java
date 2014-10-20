package parsers;

import java.nio.file.Path;

public interface DataSetParser {

	void parse(Path input) throws Exception;

}
