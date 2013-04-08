package distribution;

import math.util.FastMath;
import random.RandomGenerator;
import random.Well19937c;
import spcial.Erf;
import exception.NotStrictlyPositiveException;
import exception.NumberIsTooLargeException;
import exception.util.LocalizedFormats;

public class LogNormalDistribution extends AbstractRealDistribution {
    /** Default inverse cumulative probability accuracy. */
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1e-9;

    /** Serializable version identifier. */
    private static final long serialVersionUID = 20120112;

    /** &radic;(2 &pi;) */
    private static final double SQRT2PI = FastMath.sqrt(2 * FastMath.PI);

    /** &radic;(2) */
    private static final double SQRT2 = FastMath.sqrt(2.0);

    /** The scale parameter of this distribution. */
    private final double scale;

    /** The shape parameter of this distribution. */
    private final double shape;

    /** Inverse cumulative probability accuracy. */
    private final double solverAbsoluteAccuracy;

    /**
     * Create a log-normal distribution, where the mean and standard deviation
     * of the {@link NormalDistribution normally distributed} natural
     * logarithm of the log-normal distribution are equal to zero and one
     * respectively. In other words, the scale of the returned distribution is
     * {@code 0}, while its shape is {@code 1}.
     */
    public LogNormalDistribution() {
        this(0, 1);
    }

    /**
     * Create a log-normal distribution using the specified scale and shape.
     *
     * @param scale the scale parameter of this distribution
     * @param shape the shape parameter of this distribution
     * @throws NotStrictlyPositiveException if {@code shape <= 0}.
     */
    public LogNormalDistribution(double scale, double shape)
        throws NotStrictlyPositiveException {
        this(scale, shape, DEFAULT_INVERSE_ABSOLUTE_ACCURACY);
    }

    /**
     * Create a log-normal distribution using the specified scale, shape and
     * inverse cumulative distribution accuracy.
     *
     * @param scale the scale parameter of this distribution
     * @param shape the shape parameter of this distribution
     * @param inverseCumAccuracy Inverse cumulative probability accuracy.
     * @throws NotStrictlyPositiveException if {@code shape <= 0}.
     */
    public LogNormalDistribution(double scale, double shape, double inverseCumAccuracy)
        throws NotStrictlyPositiveException {
        this(new Well19937c(), scale, shape, inverseCumAccuracy);
    }

    /**
     * Creates a log-normal distribution.
     *
     * @param rng Random number generator.
     * @param scale Scale parameter of this distribution.
     * @param shape Shape parameter of this distribution.
     * @param inverseCumAccuracy Inverse cumulative probability accuracy.
     * @throws NotStrictlyPositiveException if {@code shape <= 0}.
     * @since 3.1
     */
    public LogNormalDistribution(RandomGenerator rng,
                                 double scale,
                                 double shape,
                                 double inverseCumAccuracy)
        throws NotStrictlyPositiveException {
        super(rng);

        if (shape <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.SHAPE, shape);
        }

        this.scale = scale;
        this.shape = shape;
        this.solverAbsoluteAccuracy = inverseCumAccuracy;
    }

    /**
     * Returns the scale parameter of this distribution.
     *
     * @return the scale parameter
     */
    public double getScale() {
        return scale;
    }

    /**
     * Returns the shape parameter of this distribution.
     *
     * @return the shape parameter
     */
    public double getShape() {
        return shape;
    }

    /**
     * {@inheritDoc}
     *
     * For scale {@code m}, and shape {@code s} of this distribution, the PDF
     * is given by
     * <ul>
     * <li>{@code 0} if {@code x <= 0},</li>
     * <li>{@code exp(-0.5 * ((ln(x) - m) / s)^2) / (s * sqrt(2 * pi) * x)}
     * otherwise.</li>
     * </ul>
     */
    public double density(double x) {
        if (x <= 0) {
            return 0;
        }
        final double x0 = FastMath.log(x) - scale;
        final double x1 = x0 / shape;
        return FastMath.exp(-0.5 * x1 * x1) / (shape * SQRT2PI * x);
    }

    /**
     * {@inheritDoc}
     *
     * For scale {@code m}, and shape {@code s} of this distribution, the CDF
     * is given by
     * <ul>
     * <li>{@code 0} if {@code x <= 0},</li>
     * <li>{@code 0} if {@code ln(x) - m < 0} and {@code m - ln(x) > 40 * s}, as
     * in these cases the actual value is within {@code Double.MIN_VALUE} of 0,
     * <li>{@code 1} if {@code ln(x) - m >= 0} and {@code ln(x) - m > 40 * s},
     * as in these cases the actual value is within {@code Double.MIN_VALUE} of
     * 1,</li>
     * <li>{@code 0.5 + 0.5 * erf((ln(x) - m) / (s * sqrt(2))} otherwise.</li>
     * </ul>
     */
    public double cumulativeProbability(double x)  {
        if (x <= 0) {
            return 0;
        }
        final double dev = FastMath.log(x) - scale;
        if (FastMath.abs(dev) > 40 * shape) {
            return dev < 0 ? 0.0d : 1.0d;
        }
        return 0.5 + 0.5 * Erf.erf(dev / (shape * SQRT2));
    }

    /**
     * {@inheritDoc}
     *
     * @deprecated See {@link RealDistribution#cumulativeProbability(double,double)}
     */
    @Override@Deprecated
    public double cumulativeProbability(double x0, double x1)
        throws NumberIsTooLargeException {
        return probability(x0, x1);
    }

    /** {@inheritDoc} */
    @Override
    public double probability(double x0,
                              double x1)
        throws NumberIsTooLargeException {
        if (x0 > x1) {
            throw new NumberIsTooLargeException(LocalizedFormats.LOWER_ENDPOINT_ABOVE_UPPER_ENDPOINT,
                                                x0, x1, true);
        }
        if (x0 <= 0 || x1 <= 0) {
            return super.probability(x0, x1);
        }
        final double denom = shape * SQRT2;
        final double v0 = (FastMath.log(x0) - scale) / denom;
        final double v1 = (FastMath.log(x1) - scale) / denom;
        return 0.5 * Erf.erf(v0, v1);
    }

    /** {@inheritDoc} */
    @Override
    protected double getSolverAbsoluteAccuracy() {
        return solverAbsoluteAccuracy;
    }

    /**
     * {@inheritDoc}
     *
     * For scale {@code m} and shape {@code s}, the mean is
     * {@code exp(m + s^2 / 2)}.
     */
    public double getNumericalMean() {
        double s = shape;
        return FastMath.exp(scale + (s * s / 2));
    }

    /**
     * {@inheritDoc}
     *
     * For scale {@code m} and shape {@code s}, the variance is
     * {@code (exp(s^2) - 1) * exp(2 * m + s^2)}.
     */
    public double getNumericalVariance() {
        final double s = shape;
        final double ss = s * s;
        return (FastMath.exp(ss) - 1) * FastMath.exp(2 * scale + ss);
    }

    /**
     * {@inheritDoc}
     *
     * The lower bound of the support is always 0 no matter the parameters.
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
     * no matter the parameters.
     *
     * @return upper bound of the support (always
     * {@code Double.POSITIVE_INFINITY})
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

    /** {@inheritDoc} */
    @Override
    public double sample()  {
        final double n = random.nextGaussian();
        return FastMath.exp(scale + shape * n);
    }
}
