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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((directory == null) ? 0 : directory.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractDataSet other = (AbstractDataSet) obj;
		if (directory == null) {
			if (other.directory != null)
				return false;
		} else if (!directory.equals(other.directory))
			return false;
		return true;
	}

	public abstract Path test();

	public abstract Path train();

	@Override
	public String toString() {
		return directory().toString();
	}

}
