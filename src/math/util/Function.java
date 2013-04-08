package math.util;

/**
 * Real-valued function that operate on an array or a part of it.
 * 
 * @since 3.1
 */
public interface Function {
	/**
	 * Operates on an entire array.
	 * 
	 * @param array
	 *            Array to operate on.
	 * @return the result of the operation.
	 */
	double evaluate(double[] array);

	/**
	 * @param array
	 *            Array to operate on.
	 * @param startIndex
	 *            Index of the first element to take into account.
	 * @param numElements
	 *            Number of elements to take into account.
	 * @return the result of the operation.
	 */
	double evaluate(double[] array, int startIndex, int numElements);
}