package models;

public class IndexFrequencyPair implements Comparable<IndexFrequencyPair> {

	private final int index;
	private final int frequency;

	public IndexFrequencyPair(int index, int frequency) {
		this.index = index;
		this.frequency = frequency;
	}

	public IndexFrequencyPair(String pair) {
		String[] array = pair.split(":");
		index = Integer.valueOf(array[0]);
		frequency = Integer.valueOf(array[1]);
	}

	public int getIndex() {
		return index;
	}

	public int getFrequency() {
		return frequency;
	}

	@Override
	public String toString() {
		return index + ":" + frequency;
	}

	@Override
	public int compareTo(IndexFrequencyPair o) {
		return Integer.compare(index, o.index);
	}

}
