package analysis.solvers;

public class IllinoisSolver extends BaseSecantSolver {

    /** Construct a solver with default accuracy (1e-6). */
    public IllinoisSolver() {
        super(DEFAULT_ABSOLUTE_ACCURACY, Method.ILLINOIS);
    }

    /**
     * Construct a solver.
     *
     * @param absoluteAccuracy Absolute accuracy.
     */
    public IllinoisSolver(final double absoluteAccuracy) {
        super(absoluteAccuracy, Method.ILLINOIS);
    }

    /**
     * Construct a solver.
     *
     * @param relativeAccuracy Relative accuracy.
     * @param absoluteAccuracy Absolute accuracy.
     */
    public IllinoisSolver(final double relativeAccuracy,
                          final double absoluteAccuracy) {
        super(relativeAccuracy, absoluteAccuracy, Method.ILLINOIS);
    }

    /**
     * Construct a solver.
     *
     * @param relativeAccuracy Relative accuracy.
     * @param absoluteAccuracy Absolute accuracy.
     * @param functionValueAccuracy Maximum function value error.
     */
    public IllinoisSolver(final double relativeAccuracy,
                          final double absoluteAccuracy,
                          final double functionValueAccuracy) {
        super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, Method.PEGASUS);
    }
}
