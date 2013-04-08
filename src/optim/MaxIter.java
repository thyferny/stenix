package optim;

import exception.NotStrictlyPositiveException;

public class MaxIter implements OptimizationData {
    /** Allowed number of evalutations. */
    private final int maxIter;

    /**
     * @param max Allowed number of iterations.
     * @throws NotStrictlyPositiveException if {@code max <= 0}.
     */
    public MaxIter(int max) {
        if (max <= 0) {
            throw new NotStrictlyPositiveException(max);
        }

        maxIter = max;
    }

    /**
     * Gets the maximum number of evaluations.
     *
     * @return the allowed number of evaluations.
     */
    public int getMaxIter() {
        return maxIter;
    }

    /**
     * Factory method that creates instance of this class that represents
     * a virtually unlimited number of iterations.
     *
     * @return a new instance suitable for allowing {@link Integer#MAX_VALUE}
     * evaluations.
     */
    public static MaxIter unlimited() {
        return new MaxIter(Integer.MAX_VALUE);
    }
}
