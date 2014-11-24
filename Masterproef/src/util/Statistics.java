package util;

import java.util.Random;

public class Statistics {

	private static final Random r = new Random();

	public static int getChanceIndex(String prob_distr) {
		String[] splitted = prob_distr.split(",");
		double random = r.nextDouble();
		double accumulator = 0;
		double largest = 0;
		int largestI = 0;
		for (int i = 0; i < splitted.length; i++) {
			double value = Double.parseDouble(splitted[i]);
			if (value > largest) {
				largest = value;
				largestI = i;
			}
			accumulator += value;
			if (accumulator >= random)
				return i;
		}
		return largestI;
	}
}
