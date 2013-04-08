package optim;

import exception.NotStrictlyPositiveException;

public class MaxEval implements OptimizationData {
    /** Allowed number of evalutations. */
    private final int maxEval;

    /**
     * @param max Allowed number of evalutations.
     * @throws NotStrictlyPositiveException if {@code max <= 0}.
     */
    public MaxEval(int max) {
        if (max <= 0) {
            throw new NotStrictlyPositiveException(max);
        }

        maxEval = max;
    }

    /**
     * Gets the maximum number of evaluations.
     *
     * @return the allowed number of evaluations.
     */
    public int getMaxEval() {
        return maxEval;
    }

    /**
     * Factory method that creates instance of this class that represents
     * a virtually unlimited number of evaluations.
     *
     * @return a new instance suitable for allowing {@link Integer#MAX_VALUE}
     * evaluations.
     */
    public static MaxEval unlimited() {
        return new MaxEval(Integer.MAX_VALUE);
    }
}
