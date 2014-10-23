package pre.parsers;

import java.nio.file.Path;

import models.DataSet;

public interface DataSetParser {

	/**
	 * @return the output path
	 */
	Path parse(Path directory) throws Exception;
	
	DataSet getData();

}
