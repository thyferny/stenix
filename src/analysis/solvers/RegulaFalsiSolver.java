package analysis.solvers;

public class RegulaFalsiSolver extends BaseSecantSolver {

    /** Construct a solver with default accuracy (1e-6). */
    public RegulaFalsiSolver() {
        super(DEFAULT_ABSOLUTE_ACCURACY, Method.REGULA_FALSI);
    }

    /**
     * Construct a solver.
     *
     * @param absoluteAccuracy Absolute accuracy.
     */
    public RegulaFalsiSolver(final double absoluteAccuracy) {
        super(absoluteAccuracy, Method.REGULA_FALSI);
    }

    /**
     * Construct a solver.
     *
     * @param relativeAccuracy Relative accuracy.
     * @param absoluteAccuracy Absolute accuracy.
     */
    public RegulaFalsiSolver(final double relativeAccuracy,
                             final double absoluteAccuracy) {
        super(relativeAccuracy, absoluteAccuracy, Method.REGULA_FALSI);
    }

    /**
     * Construct a solver.
     *
     * @param relativeAccuracy Relative accuracy.
     * @param absoluteAccuracy Absolute accuracy.
     * @param functionValueAccuracy Maximum function value error.
     */
    public RegulaFalsiSolver(final double relativeAccuracy,
                             final double absoluteAccuracy,
                             final double functionValueAccuracy) {
        super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, Method.REGULA_FALSI);
    }
}
