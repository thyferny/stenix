package analysis.interpolation;

import math.util.MathArrays;
import math.util.MathUtils;
import analysis.UnivariateFunction;
import exception.NumberIsTooSmallException;

public class UnivariatePeriodicInterpolator
    implements UnivariateInterpolator {
    /** Default number of extension points of the samples array. */
    public static final int DEFAULT_EXTEND = 5;
    /** Interpolator. */
    private final UnivariateInterpolator interpolator;
    /** Period. */
    private final double period;
    /** Number of extension points. */
    private final int extend;

    /**
     * Builds an interpolator.
     *
     * @param interpolator Interpolator.
     * @param period Period.
     * @param extend Number of points to be appended at the beginning and
     * end of the sample arrays in order to avoid interpolation failure at
     * the (periodic) boundaries of the orginal interval. The value is the
     * number of sample points which the original {@code interpolator} needs
     * on each side of the interpolated point.
     */
    public UnivariatePeriodicInterpolator(UnivariateInterpolator interpolator,
                                          double period,
                                          int extend) {
        this.interpolator = interpolator;
        this.period = period;
        this.extend = extend;
    }

    /**
     * Builds an interpolator.
     * Uses {@link #DEFAULT_EXTEND} as the number of extension points on each side
     * of the original abscissae range.
     *
     * @param interpolator Interpolator.
     * @param period Period.
     */
    public UnivariatePeriodicInterpolator(UnivariateInterpolator interpolator,
                                          double period) {
        this(interpolator, period, DEFAULT_EXTEND);
    }

    /**
     * {@inheritDoc}
     *
     * @throws NumberIsTooSmallException if the number of extension points
     * iss larger then the size of {@code xval}.
     */
    public UnivariateFunction interpolate(double[] xval,
                                          double[] yval)
        throws NumberIsTooSmallException {
        if (xval.length < extend) {
            throw new NumberIsTooSmallException(xval.length, extend, true);
        }

        MathArrays.checkOrder(xval);
        final double offset = xval[0];

        final int len = xval.length + extend * 2;
        final double[] x = new double[len];
        final double[] y = new double[len];
        for (int i = 0; i < xval.length; i++) {
            final int index = i + extend;
            x[index] = MathUtils.reduce(xval[i], period, offset);
            y[index] = yval[i];
        }

        // Wrap to enable interpolation at the boundaries.
        for (int i = 0; i < extend; i++) {
            int index = xval.length - extend + i;
            x[i] = MathUtils.reduce(xval[index], period, offset) - period;
            y[i] = yval[index];

            index = len - extend + i;
            x[index] = MathUtils.reduce(xval[i], period, offset) + period;
            y[index] = yval[i];
        }

        MathArrays.sortInPlace(x, y);

        final UnivariateFunction f = interpolator.interpolate(x, y);
        return new UnivariateFunction() {
            public double value(final double x) {
                return f.value(MathUtils.reduce(x, period, offset));
            }
        };
    }
}
