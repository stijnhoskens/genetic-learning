package text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

class AbstractDictionary<T extends CharSequence> {

	protected final List<T> terms = new ArrayList<>();

	AbstractDictionary(Collection<T> terms, Comparator<? super T> c) {
		this.terms.addAll(terms);
		this.terms.sort(c);
	}

	AbstractDictionary(Collection<T> terms) {
		this.terms.addAll(terms);
	}

	void export(Path path) throws IOException {
		Files.write(path, terms);
	}

	T getWord(int index) throws IndexOutOfBoundsException {
		return terms.get(index);
	}

	int indexOf(String word) {
		return terms.indexOf(word);
	}

}
