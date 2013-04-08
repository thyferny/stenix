package analysis.solvers;

import math.util.FastMath;
import exception.NoBracketingException;
import exception.NumberIsTooLargeException;
import exception.TooManyEvaluationsException;

public class MullerSolver2 extends AbstractUnivariateSolver {

    /** Default absolute accuracy. */
    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1e-6;

    /**
     * Construct a solver with default accuracy (1e-6).
     */
    public MullerSolver2() {
        this(DEFAULT_ABSOLUTE_ACCURACY);
    }
    /**
     * Construct a solver.
     *
     * @param absoluteAccuracy Absolute accuracy.
     */
    public MullerSolver2(double absoluteAccuracy) {
        super(absoluteAccuracy);
    }
    /**
     * Construct a solver.
     *
     * @param relativeAccuracy Relative accuracy.
     * @param absoluteAccuracy Absolute accuracy.
     */
    public MullerSolver2(double relativeAccuracy,
                        double absoluteAccuracy) {
        super(relativeAccuracy, absoluteAccuracy);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected double doSolve()
        throws TooManyEvaluationsException,
               NumberIsTooLargeException,
               NoBracketingException {
        final double min = getMin();
        final double max = getMax();

        verifyInterval(min, max);

        final double relativeAccuracy = getRelativeAccuracy();
        final double absoluteAccuracy = getAbsoluteAccuracy();
        final double functionValueAccuracy = getFunctionValueAccuracy();

        // x2 is the last root approximation
        // x is the new approximation and new x2 for next round
        // x0 < x1 < x2 does not hold here

        double x0 = min;
        double y0 = computeObjectiveValue(x0);
        if (FastMath.abs(y0) < functionValueAccuracy) {
            return x0;
        }
        double x1 = max;
        double y1 = computeObjectiveValue(x1);
        if (FastMath.abs(y1) < functionValueAccuracy) {
            return x1;
        }

        if(y0 * y1 > 0) {
            throw new NoBracketingException(x0, x1, y0, y1);
        }

        double x2 = 0.5 * (x0 + x1);
        double y2 = computeObjectiveValue(x2);

        double oldx = Double.POSITIVE_INFINITY;
        while (true) {
            // quadratic interpolation through x0, x1, x2
            final double q = (x2 - x1) / (x1 - x0);
            final double a = q * (y2 - (1 + q) * y1 + q * y0);
            final double b = (2 * q + 1) * y2 - (1 + q) * (1 + q) * y1 + q * q * y0;
            final double c = (1 + q) * y2;
            final double delta = b * b - 4 * a * c;
            double x;
            final double denominator;
            if (delta >= 0.0) {
                // choose a denominator larger in magnitude
                double dplus = b + FastMath.sqrt(delta);
                double dminus = b - FastMath.sqrt(delta);
                denominator = FastMath.abs(dplus) > FastMath.abs(dminus) ? dplus : dminus;
            } else {
                // take the modulus of (B +/- FastMath.sqrt(delta))
                denominator = FastMath.sqrt(b * b - delta);
            }
            if (denominator != 0) {
                x = x2 - 2.0 * c * (x2 - x1) / denominator;
                // perturb x if it exactly coincides with x1 or x2
                // the equality tests here are intentional
                while (x == x1 || x == x2) {
                    x += absoluteAccuracy;
                }
            } else {
                // extremely rare case, get a random number to skip it
                x = min + FastMath.random() * (max - min);
                oldx = Double.POSITIVE_INFINITY;
            }
            final double y = computeObjectiveValue(x);

            // check for convergence
            final double tolerance = FastMath.max(relativeAccuracy * FastMath.abs(x), absoluteAccuracy);
            if (FastMath.abs(x - oldx) <= tolerance ||
                FastMath.abs(y) <= functionValueAccuracy) {
                return x;
            }

            // prepare the next iteration
            x0 = x1;
            y0 = y1;
            x1 = x2;
            y1 = y2;
            x2 = x;
            y2 = y;
            oldx = x;
        }
    }
}
