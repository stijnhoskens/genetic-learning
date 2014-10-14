package text;

public class WordFrequencyPair implements CharSequence,
		Comparable<WordFrequencyPair> {

	private final String pair;

	public WordFrequencyPair(String pair) {
		this.pair = pair;
	}

	public WordFrequencyPair(String word, int freq) {
		this.pair = word + ":" + freq;
	}

	public String getWord() {
		return pair.split(":")[0];
	}

	public int getFrequency() {
		return Integer.parseInt(pair.split(":")[1]);
	}

	@Override
	public String toString() {
		return pair;
	}

	@Override
	public int length() {
		return pair.length();
	}

	@Override
	public char charAt(int index) {
		return pair.charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return pair.subSequence(start, end);
	}

	@Override
	public int compareTo(WordFrequencyPair o) {
		return pair.compareTo(o.pair);
	}

}
