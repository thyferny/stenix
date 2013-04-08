package optim.univariate;

import optim.OptimizationData;
import exception.NumberIsTooLargeException;
import exception.OutOfRangeException;

public class SearchInterval implements OptimizationData {
    /** Lower bound. */
    private final double lower;
    /** Upper bound. */
    private final double upper;
    /** Start value. */
    private final double start;

    /**
     * @param lo Lower bound.
     * @param hi Upper bound.
     * @param init Start value.
     * @throws NumberIsTooLargeException if {@code lo >= hi}.
     * @throws OutOfRangeException if {@code init < lo} or {@code init > hi}.
     */
    public SearchInterval(double lo,
                          double hi,
                          double init) {
        if (lo >= hi) {
            throw new NumberIsTooLargeException(lo, hi, false);
        }
        if (init < lo ||
            init > hi) {
            throw new OutOfRangeException(init, lo, hi);
        }

        lower = lo;
        upper = hi;
        start = init;
    }

    /**
     * @param lo Lower bound.
     * @param hi Upper bound.
     * @throws NumberIsTooLargeException if {@code lo >= hi}.
     */
    public SearchInterval(double lo,
                          double hi) {
        this(lo, hi, 0.5 * (lo + hi));
    }

    /**
     * Gets the lower bound.
     *
     * @return the lower bound.
     */
    public double getMin() {
        return lower;
    }
    /**
     * Gets the upper bound.
     *
     * @return the upper bound.
     */
    public double getMax() {
        return upper;
    }
    /**
     * Gets the start value.
     *
     * @return the start value.
     */
    public double getStartValue() {
        return start;
    }
}
