package analysis.solvers;

public class PegasusSolver extends BaseSecantSolver {

    /** Construct a solver with default accuracy (1e-6). */
    public PegasusSolver() {
        super(DEFAULT_ABSOLUTE_ACCURACY, Method.PEGASUS);
    }

    /**
     * Construct a solver.
     *
     * @param absoluteAccuracy Absolute accuracy.
     */
    public PegasusSolver(final double absoluteAccuracy) {
        super(absoluteAccuracy, Method.PEGASUS);
    }

    /**
     * Construct a solver.
     *
     * @param relativeAccuracy Relative accuracy.
     * @param absoluteAccuracy Absolute accuracy.
     */
    public PegasusSolver(final double relativeAccuracy,
                         final double absoluteAccuracy) {
        super(relativeAccuracy, absoluteAccuracy, Method.PEGASUS);
    }

    /**
     * Construct a solver.
     *
     * @param relativeAccuracy Relative accuracy.
     * @param absoluteAccuracy Absolute accuracy.
     * @param functionValueAccuracy Maximum function value error.
     */
    public PegasusSolver(final double relativeAccuracy,
                         final double absoluteAccuracy,
                         final double functionValueAccuracy) {
        super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, Method.PEGASUS);
    }
}
