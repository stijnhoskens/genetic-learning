package text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

class AbstractVocabulary<T extends CharSequence> {

	protected final List<T> terms = new ArrayList<>();
	private final Comparator<? super T> comp;
	private final BinarySearcher searcher;

	AbstractVocabulary(Collection<T> terms, Comparator<? super T> c,
			boolean isSorted) {
		this.terms.addAll(terms);
		this.comp = c;
		if (!isSorted)
			this.terms.sort(c);
		searcher = new BinarySearcher();
	}

	public void export(Path path) throws IOException {
		Files.write(path, terms);
	}

	public T getWord(int index) throws IndexOutOfBoundsException {
		return terms.get(index);
	}

	public int indexOf(String word) {
		return terms.indexOf(word);
	}

	public int binarySearchIndexOf(T term) {
		return searcher.search(term);
	}

	private class BinarySearcher {
		private T[] array;

		@SuppressWarnings("unchecked")
		public BinarySearcher() {
			array = (T[]) terms.toArray();
		}

		public int search(T t) {
			return Arrays.binarySearch(array, t);
		}
	}

	public AbstractVocabulary<T> merge(AbstractVocabulary<T> other) {
		List<T> terms = new ArrayList<>();
		terms.addAll(this.terms);
		terms.addAll(other.terms);
		terms.sort(comp);
		return new AbstractVocabulary<>(terms, comp, true);
	}
}
