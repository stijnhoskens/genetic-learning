package text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

abstract class AbstractVocabulary<T extends CharSequence> {

	protected final List<T> terms = new ArrayList<>();

	AbstractVocabulary(Collection<T> terms, Comparator<? super T> c,
			boolean isSorted) {
		this.terms.addAll(terms);
		if (!isSorted)
			this.terms.sort(c);
	}

	public void export(Path path) throws IOException {
		Files.write(path, terms);
	}

	public T getTerm(int index) throws IndexOutOfBoundsException {
		return terms.get(index);
	}

	public abstract int indexOf(String word);
}
