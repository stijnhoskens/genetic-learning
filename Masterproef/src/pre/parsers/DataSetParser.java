package pre.parsers;

import java.nio.file.Path;

import datasets.FullDataSet;

public interface DataSetParser {

	/**
	 * @return the output path
	 */
	Path parse(Path directory) throws Exception;
	
	FullDataSet getData();

}
