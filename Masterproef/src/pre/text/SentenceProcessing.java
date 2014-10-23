package pre.text;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class SentenceProcessing {

	/**
	 * Returns an alphabetically sorted list of the words in the given sentence.
	 */
	public static List<String> toListOfWords(String sentence) {
		List<String> words = new ArrayList<>();
		BreakIterator wordsIterator = BreakIterator.getWordInstance();
		wordsIterator.setText(sentence);
		int start = wordsIterator.first();
		int end = wordsIterator.next();
		while (end != BreakIterator.DONE) {
			String word = sentence.substring(start, end);
			if (Character.isLetter(word.charAt(0))) {
				words.add(word.toLowerCase());
			}
			start = end;
			end = wordsIterator.next();
		}
		words.sort(Comparator.naturalOrder());
		return words;
	}

	/**
	 * Returns the given sentence as a processed string. For instance, the
	 * sentence "I am a programmer." is returned as "a:1 am:1 i:1 programmer:1".
	 * It is in the form "word:frequency" separated by spaces. These terms are
	 * also sorted alphabetically.
	 * 
	 * @param sentence
	 * @return
	 */
	public static String toProcessedString(String sentence) {
		List<String> words = toListOfWords(sentence);
		if (words.size() == 0)
			return "";
		StringBuilder buffer = new StringBuilder();
		String previousWord = words.get(0);
		int currentCount = 0;
		for (int i = 1; i < words.size(); i++) {
			String word = words.get(i);
			currentCount++;
			if (!word.equals(previousWord)) {
				buffer.append(previousWord + ":" + currentCount + " ");
				previousWord = word;
				currentCount = 0;
			}
		}
		buffer.append(words.get(words.size() - 1) + ":" + ++currentCount);
		return buffer.toString();
	}

}
