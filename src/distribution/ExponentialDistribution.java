package distribution;

import math.util.ArithmeticUtils;
import math.util.FastMath;
import math.util.ResizableDoubleArray;
import random.RandomGenerator;
import random.Well19937c;
import exception.NotStrictlyPositiveException;
import exception.OutOfRangeException;
import exception.util.LocalizedFormats;

public class ExponentialDistribution extends AbstractRealDistribution {
    /**
     * Default inverse cumulative probability accuracy.
     * @since 2.1
     */
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1e-9;
    /** Serializable version identifier */
    private static final long serialVersionUID = 2401296428283614780L;
    /**
     * Used when generating Exponential samples.
     * Table containing the constants
     * q_i = sum_{j=1}^i (ln 2)^j/j! = ln 2 + (ln 2)^2/2 + ... + (ln 2)^i/i!
     * until the largest representable fraction below 1 is exceeded.
     *
     * Note that
     * 1 = 2 - 1 = exp(ln 2) - 1 = sum_{n=1}^infty (ln 2)^n / n!
     * thus q_i -> 1 as i -> +inf,
     * so the higher i, the closer to one we get (the series is not alternating).
     *
     * By trying, n = 16 in Java is enough to reach 1.0.
     */
    private static final double[] EXPONENTIAL_SA_QI;
    /** The mean of this distribution. */
    private final double mean;
    /** Inverse cumulative probability accuracy. */
    private final double solverAbsoluteAccuracy;

    /**
     * Initialize tables.
     */
    static {
        /**
         * Filling EXPONENTIAL_SA_QI table.
         * Note that we don't want qi = 0 in the table.
         */
        final double LN2 = FastMath.log(2);
        double qi = 0;
        int i = 1;

        /**
         * ArithmeticUtils provides factorials up to 20, so let's use that
         * limit together with Precision.EPSILON to generate the following
         * code (a priori, we know that there will be 16 elements, but it is
         * better to not hardcode it).
         */
        final ResizableDoubleArray ra = new ResizableDoubleArray(20);

        while (qi < 1) {
            qi += FastMath.pow(LN2, i) / ArithmeticUtils.factorial(i);
            ra.addElement(qi);
            ++i;
        }

        EXPONENTIAL_SA_QI = ra.getElements();
    }

    /**
     * Create an exponential distribution with the given mean.
     * @param mean mean of this distribution.
     */
    public ExponentialDistribution(double mean) {
        this(mean, DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
    }

    /**
     * Create an exponential distribution with the given mean.
     *
     * @param mean Mean of this distribution.
     * @param inverseCumAccuracy Maximum absolute error in inverse
     * cumulative probability estimates (defaults to
     * {@link #DEFAULT_INVERSE_ABSOLUTE_ACCURACY}).
     * @throws NotStrictlyPositiveException if {@code mean <= 0}.
     * @since 2.1
     */
    public ExponentialDistribution(double mean, double inverseCumAccuracy) {
        this(new Well19937c(), mean, inverseCumAccuracy);
    }

    /**
     * Creates an exponential distribution.
     *
     * @param rng Random number generator.
     * @param mean Mean of this distribution.
     * @param inverseCumAccuracy Maximum absolute error in inverse
     * cumulative probability estimates (defaults to
     * {@link #DEFAULT_INVERSE_ABSOLUTE_ACCURACY}).
     * @throws NotStrictlyPositiveException if {@code mean <= 0}.
     * @since 3.1
     */
    public ExponentialDistribution(RandomGenerator rng,
                                   double mean,
                                   double inverseCumAccuracy)
        throws NotStrictlyPositiveException {
        super(rng);

        if (mean <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.MEAN, mean);
        }
        this.mean = mean;
        solverAbsoluteAccuracy = inverseCumAccuracy;
    }

    /**
     * Access the mean.
     *
     * @return the mean.
     */
    public double getMean() {
        return mean;
    }

    /** {@inheritDoc} */
    public double density(double x) {
        if (x < 0) {
            return 0;
        }
        return FastMath.exp(-x / mean) / mean;
    }

    /**
     * {@inheritDoc}
     *
     * The implementation of this method is based on:
     * <ul>
     * <li>
     * <a href="http://mathworld.wolfram.com/ExponentialDistribution.html">
     * Exponential Distribution</a>, equation (1).</li>
     * </ul>
     */
    public double cumulativeProbability(double x)  {
        double ret;
        if (x <= 0.0) {
            ret = 0.0;
        } else {
            ret = 1.0 - FastMath.exp(-x / mean);
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     *
     * Returns {@code 0} when {@code p= = 0} and
     * {@code Double.POSITIVE_INFINITY} when {@code p == 1}.
     */
    @Override
    public double inverseCumulativeProbability(double p) throws OutOfRangeException {
        double ret;

        if (p < 0.0 || p > 1.0) {
            throw new OutOfRangeException(p, 0.0, 1.0);
        } else if (p == 1.0) {
            ret = Double.POSITIVE_INFINITY;
        } else {
            ret = -mean * FastMath.log(1.0 - p);
        }

        return ret;
    }

    /**
     * {@inheritDoc}
     *
     * <p><strong>Algorithm Description</strong>: this implementation uses the
     * <a href="http://www.jesus.ox.ac.uk/~clifford/a5/chap1/node5.html">
     * Inversion Method</a> to generate exponentially distributed random values
     * from uniform deviates.</p>
     *
     * @return a random value.
     * @since 2.2
     */
    @Override
    public double sample() {
        // Step 1:
        double a = 0;
        double u = random.nextDouble();

        // Step 2 and 3:
        while (u < 0.5) {
            a += EXPONENTIAL_SA_QI[0];
            u *= 2;
        }

        // Step 4 (now u >= 0.5):
        u += u - 1;

        // Step 5:
        if (u <= EXPONENTIAL_SA_QI[0]) {
            return mean * (a + u);
        }

        // Step 6:
        int i = 0; // Should be 1, be we iterate before it in while using 0
        double u2 = random.nextDouble();
        double umin = u2;

        // Step 7 and 8:
        do {
            ++i;
            u2 = random.nextDouble();

            if (u2 < umin) {
                umin = u2;
            }

            // Step 8:
        } while (u > EXPONENTIAL_SA_QI[i]); // Ensured to exit since EXPONENTIAL_SA_QI[MAX] = 1

        return mean * (a + umin * EXPONENTIAL_SA_QI[0]);
    }

    /** {@inheritDoc} */
    @Override
    protected double getSolverAbsoluteAccuracy() {
        return solverAbsoluteAccuracy;
    }

    /**
     * {@inheritDoc}
     *
     * For mean parameter {@code k}, the mean is {@code k}.
     */
    public double getNumericalMean() {
        return getMean();
    }

    /**
     * {@inheritDoc}
     *
     * For mean parameter {@code k}, the variance is {@code k^2}.
     */
    public double getNumericalVariance() {
        final double m = getMean();
        return m * m;
    }

    /**
     * {@inheritDoc}
     *
     * The lower bound of the support is always 0 no matter the mean parameter.
     *
     * @return lower bound of the support (always 0)
     */
    public double getSupportLowerBound() {
        return 0;
    }

    /**
     * {@inheritDoc}
     *
     * The upper bound of the support is always positive infinity
     * no matter the mean parameter.
     *
     * @return upper bound of the support (always Double.POSITIVE_INFINITY)
     */
    public double getSupportUpperBound() {
        return Double.POSITIVE_INFINITY;
    }

    /** {@inheritDoc} */
    public boolean isSupportLowerBoundInclusive() {
        return true;
    }

    /** {@inheritDoc} */
    public boolean isSupportUpperBoundInclusive() {
        return false;
    }

    /**
     * {@inheritDoc}
     *
     * The support of this distribution is connected.
     *
     * @return {@code true}
     */
    public boolean isSupportConnected() {
        return true;
    }
}
