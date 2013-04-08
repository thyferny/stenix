package analysis.integration.gauss;

import math.util.MathArrays;
import math.util.Pair;
import analysis.UnivariateFunction;
import exception.DimensionMismatchException;
import exception.NonMonotonicSequenceException;

public class GaussIntegrator {
    /** Nodes. */
    private final double[] points;
    /** Nodes weights. */
    private final double[] weights;

    /**
     * Creates an integrator from the given {@code points} and {@code weights}.
     * The integration interval is defined by the first and last value of
     * {@code points} which must be sorted in increasing order.
     *
     * @param points Integration points.
     * @param weights Weights of the corresponding integration nodes.
     * @throws NonMonotonicSequenceException if the {@code points} are not
     * sorted in increasing order.
     */
    public GaussIntegrator(double[] points,
                           double[] weights)
        throws NonMonotonicSequenceException {
        if (points.length != weights.length) {
            throw new DimensionMismatchException(points.length,
                                                 weights.length);
        }

        MathArrays.checkOrder(points, MathArrays.OrderDirection.INCREASING, true, true);

        this.points = points.clone();
        this.weights = weights.clone();
    }

    /**
     * Creates an integrator from the given pair of points (first element of
     * the pair) and weights (second element of the pair.
     *
     * @param pointsAndWeights Integration points and corresponding weights.
     * @throws NonMonotonicSequenceException if the {@code points} are not
     * sorted in increasing order.
     *
     * @see #GaussIntegrator(double[], double[])
     */
    public GaussIntegrator(Pair<double[], double[]> pointsAndWeights)
        throws NonMonotonicSequenceException {
        this(pointsAndWeights.getFirst(), pointsAndWeights.getSecond());
    }

    /**
     * Returns an estimate of the integral of {@code f(x) * w(x)},
     * where {@code w} is a weight function that depends on the actual
     * flavor of the Gauss integration scheme.
     * The algorithm uses the points and associated weights, as passed
     * to the {@link #GaussIntegrator(double[],double[]) constructor}.
     *
     * @param f Function to integrate.
     * @return the integral of the weighted function.
     */
    public double integrate(UnivariateFunction f) {
        double s = 0;
        double c = 0;
        for (int i = 0; i < points.length; i++) {
            final double x = points[i];
            final double w = weights[i];
            final double y = w * f.value(x) - c;
            final double t = s + y;
            c = (t - s) - y;
            s = t;
        }
        return s;
    }

    /**
     * @return the order of the integration rule (the number of integration
     * points).
     */
    public int getNumberOfPoints() {
        return points.length;
    }
}
