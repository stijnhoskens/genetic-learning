package exceptions;

public class PopulationSizeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final int prevSize;
	private final int currSize;
	
	public PopulationSizeException(int legalSize, int attemptedSize) {
		prevSize = legalSize;
		currSize = attemptedSize;
	}

	public int getLegalSize() {
		return prevSize;
	}

	public int getAttemptedSize() {
		return currSize;
	}

}
