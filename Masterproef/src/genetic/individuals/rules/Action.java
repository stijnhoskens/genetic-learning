package genetic.individuals.rules;

import java.util.function.Supplier;

public class Action implements Supplier<String> {

	private final String clsfrName;

	public Action(String classifierName) {
		clsfrName = classifierName;
	}

	public String get() {
		return clsfrName;
	}

}
