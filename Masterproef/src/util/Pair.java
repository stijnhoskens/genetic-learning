package util;

import java.util.Arrays;
import java.util.List;

public class Pair<F, S> {

	private final F f;
	private final S s;

	public Pair(F first, S second) {
		f = first;
		s = second;
	}

	public F getFirst() {
		return f;
	}

	public S getSecond() {
		return s;
	}

	public static <T> List<T> asList(Pair<T, T> pair) {
		return Arrays.asList(pair.getFirst(), pair.getSecond());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((f == null) ? 0 : f.hashCode());
		result = prime * result + ((s == null) ? 0 : s.hashCode());
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
		Pair<?, ?> other = (Pair<?, ?>) obj;
		if (f == null) {
			if (other.f != null)
				return false;
		} else if (!f.equals(other.f))
			return false;
		if (s == null) {
			if (other.s != null)
				return false;
		} else if (!s.equals(other.s))
			return false;
		return true;
	}

}
