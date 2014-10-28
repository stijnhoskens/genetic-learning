package pre.parsers;

import java.nio.file.Path;

import pre.models.FullDataSet;

public interface DataSetParser {

	/**
	 * @return the output path
	 */
	Path parse(Path directory) throws Exception;
	
	FullDataSet getData();

}
