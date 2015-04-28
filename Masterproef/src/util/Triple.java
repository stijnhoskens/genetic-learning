package util;

import java.util.Arrays;
import java.util.List;

public class Triple<F, S, T> extends Pair<F, S> {

	protected final T t;

	public Triple(F first, S second, T third) {
		super(first, second);
		t = third;
	}

	public T getThird() {
		return t;
	}

	public static <T> List<T> asList(Triple<T, T, T> triple) {
		return Arrays.asList(triple.getFirst(), triple.getSecond(),
				triple.getThird());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((t == null) ? 0 : t.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Triple<?, ?, ?> other = (Triple<?, ?, ?>) obj;
		if (t == null) {
			if (other.t != null)
				return false;
		} else if (!t.equals(other.t))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return super.toString() + ", " + t;
	}

}
