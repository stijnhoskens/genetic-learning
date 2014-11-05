package datasets;

import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class AbstractDataSet {

	protected final String directory;

	public AbstractDataSet(String directory) {
		this.directory = directory;
	}

	public AbstractDataSet(Path directory) {
		this.directory = directory.toString();
	}

	public Path directory() {
		return Paths.get(directory);
	}

	public abstract Path test();

	public abstract Path train();

	@Override
	public String toString() {
		return directory().toString();
	}

}
