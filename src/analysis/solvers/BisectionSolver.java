package analysis.solvers;

import math.util.FastMath;
import exception.TooManyEvaluationsException;

public class BisectionSolver extends AbstractUnivariateSolver {
    /** Default absolute accuracy. */
    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1e-6;

    /**
     * Construct a solver with default accuracy (1e-6).
     */
    public BisectionSolver() {
        this(DEFAULT_ABSOLUTE_ACCURACY);
    }
    /**
     * Construct a solver.
     *
     * @param absoluteAccuracy Absolute accuracy.
     */
    public BisectionSolver(double absoluteAccuracy) {
        super(absoluteAccuracy);
    }
    /**
     * Construct a solver.
     *
     * @param relativeAccuracy Relative accuracy.
     * @param absoluteAccuracy Absolute accuracy.
     */
    public BisectionSolver(double relativeAccuracy,
                           double absoluteAccuracy) {
        super(relativeAccuracy, absoluteAccuracy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected double doSolve()
        throws TooManyEvaluationsException {
        double min = getMin();
        double max = getMax();
        verifyInterval(min, max);
        final double absoluteAccuracy = getAbsoluteAccuracy();
        double m;
        double fm;
        double fmin;

        while (true) {
            m = UnivariateSolverUtils.midpoint(min, max);
            fmin = computeObjectiveValue(min);
            fm = computeObjectiveValue(m);

            if (fm * fmin > 0) {
                // max and m bracket the root.
                min = m;
            } else {
                // min and m bracket the root.
                max = m;
            }

            if (FastMath.abs(max - min) <= absoluteAccuracy) {
                m = UnivariateSolverUtils.midpoint(min, max);
                return m;
            }
        }
    }
}
