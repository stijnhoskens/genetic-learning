package pre.models;

public class WordFrequencyPair implements CharSequence,
		Comparable<WordFrequencyPair> {

	private final String pair;
	private final String word;
	private final int freq;

	public WordFrequencyPair(String pair) {
		this.pair = pair;
		word = pair.split(":")[0];
		freq = Integer.parseInt(pair.split(":")[1]);
	}

	public WordFrequencyPair(String word, int freq) {
		pair = word + ":" + freq;
		this.word = word;
		this.freq = freq;
	}

	public String getWord() {
		return word;
	}

	public int getFrequency() {
		return freq;
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
		return getWord().compareTo(o.getWord());
	}

}
