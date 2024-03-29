package analysis.integration;

import math.util.FastMath;
import analysis.UnivariateFunction;
import analysis.integration.gauss.GaussIntegrator;
import analysis.integration.gauss.GaussIntegratorFactory;
import exception.MaxCountExceededException;
import exception.NotStrictlyPositiveException;
import exception.NumberIsTooSmallException;
import exception.TooManyEvaluationsException;

public class IterativeLegendreGaussIntegrator
    extends BaseAbstractUnivariateIntegrator {
    /** Factory that computes the points and weights. */
    private static final GaussIntegratorFactory FACTORY
        = new GaussIntegratorFactory();
    /** Number of integration points (per interval). */
    private final int numberOfPoints;

    /**
     * Builds an integrator with given accuracies and iterations counts.
     *
     * @param n Number of integration points.
     * @param relativeAccuracy Relative accuracy of the result.
     * @param absoluteAccuracy Absolute accuracy of the result.
     * @param minimalIterationCount Minimum number of iterations.
     * @param maximalIterationCount Maximum number of iterations.
     * @throws NotStrictlyPositiveException if minimal number of iterations
     * is not strictly positive.
     * @throws NumberIsTooSmallException if maximal number of iterations
     * is smaller than or equal to the minimal number of iterations.
     */
    public IterativeLegendreGaussIntegrator(final int n,
                                            final double relativeAccuracy,
                                            final double absoluteAccuracy,
                                            final int minimalIterationCount,
                                            final int maximalIterationCount)
        throws NotStrictlyPositiveException, NumberIsTooSmallException {
        super(relativeAccuracy, absoluteAccuracy, minimalIterationCount, maximalIterationCount);
        numberOfPoints = n;
    }

    /**
     * Builds an integrator with given accuracies.
     *
     * @param n Number of integration points.
     * @param relativeAccuracy Relative accuracy of the result.
     * @param absoluteAccuracy Absolute accuracy of the result.
     */
    public IterativeLegendreGaussIntegrator(final int n,
                                            final double relativeAccuracy,
                                            final double absoluteAccuracy) {
        this(n, relativeAccuracy, absoluteAccuracy,
             DEFAULT_MIN_ITERATIONS_COUNT, DEFAULT_MAX_ITERATIONS_COUNT);
    }

    /**
     * Builds an integrator with given iteration counts.
     *
     * @param n Number of integration points.
     * @param minimalIterationCount Minimum number of iterations.
     * @param maximalIterationCount Maximum number of iterations.
     * @throws NotStrictlyPositiveException if minimal number of iterations
     * is not strictly positive.
     * @throws NumberIsTooSmallException if maximal number of iterations
     * is smaller than or equal to the minimal number of iterations.
     */
    public IterativeLegendreGaussIntegrator(final int n,
                                            final int minimalIterationCount,
                                            final int maximalIterationCount) {
        this(n, DEFAULT_RELATIVE_ACCURACY, DEFAULT_ABSOLUTE_ACCURACY,
             minimalIterationCount, maximalIterationCount);
    }

    /** {@inheritDoc} */
    @Override
    protected double doIntegrate()
        throws TooManyEvaluationsException, MaxCountExceededException {
        // Compute first estimate with a single step.
        double oldt = stage(1);

        int n = 2;
        while (true) {
            // Improve integral with a larger number of steps.
            final double t = stage(n);

            // Estimate the error.
            final double delta = FastMath.abs(t - oldt);
            final double limit =
                FastMath.max(getAbsoluteAccuracy(),
                             getRelativeAccuracy() * (FastMath.abs(oldt) + FastMath.abs(t)) * 0.5);

            // check convergence
            if (iterations.getCount() + 1 >= getMinimalIterationCount() &&
                delta <= limit) {
                return t;
            }

            // Prepare next iteration.
            final double ratio = FastMath.min(4, FastMath.pow(delta / limit, 0.5 / numberOfPoints));
            n = FastMath.max((int) (ratio * n), n + 1);
            oldt = t;
            iterations.incrementCount();
        }
    }

    /**
     * Compute the n-th stage integral.
     *
     * @param n Number of steps.
     * @return the value of n-th stage integral.
     * @throws TooManyEvaluationsException if the maximum number of evaluations
     * is exceeded.
     */
    private double stage(final int n)
        throws TooManyEvaluationsException {
        // Function to be integrated is stored in the base class.
        final UnivariateFunction f = new UnivariateFunction() {
                public double value(double x) {
                    return computeObjectiveValue(x);
                }
            };

        final double min = getMin();
        final double max = getMax();
        final double step = (max - min) / n;

        double sum = 0;
        for (int i = 0; i < n; i++) {
            // Integrate over each sub-interval [a, b].
            final double a = min + i * step;
            final double b = a + step;
            final GaussIntegrator g = FACTORY.legendreHighPrecision(numberOfPoints, a, b);
            sum += g.integrate(f);
        }

        return sum;
    }
}
