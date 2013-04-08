package stat.descriptive.moment;

import java.io.Serializable;

import math.util.MathUtils;
import exception.NullArgumentException;


class ThirdMoment extends SecondMoment implements Serializable {

    /** Serializable version identifier */
    private static final long serialVersionUID = -7818711964045118679L;

    /** third moment of values that have been added */
    protected double m3;

     /**
     * Square of deviation of most recently added value from previous first
     * moment, normalized by previous sample size.  Retained to prevent
     * repeated computation in higher order moments.  nDevSq = nDev * nDev.
     */
    protected double nDevSq;

    /**
     * Create a FourthMoment instance
     */
    public ThirdMoment() {
        super();
        m3 = Double.NaN;
        nDevSq = Double.NaN;
    }

    /**
     * Copy constructor, creates a new {@code ThirdMoment} identical
     * to the {@code original}
     *
     * @param original the {@code ThirdMoment} instance to copy
     * @throws NullArgumentException if orginal is null
     */
    public ThirdMoment(ThirdMoment original) throws NullArgumentException {
        copy(original, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increment(final double d) {
        if (n < 1) {
            m3 = m2 = m1 = 0.0;
        }

        double prevM2 = m2;
        super.increment(d);
        nDevSq = nDev * nDev;
        double n0 = n;
        m3 = m3 - 3.0 * nDev * prevM2 + (n0 - 1) * (n0 - 2) * nDevSq * dev;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getResult() {
        return m3;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        super.clear();
        m3 = Double.NaN;
        nDevSq = Double.NaN;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ThirdMoment copy() {
        ThirdMoment result = new ThirdMoment();
        // No try-catch or advertised exception because args are guaranteed non-null
        copy(this, result);
        return result;
    }

    /**
     * Copies source to dest.
     * <p>Neither source nor dest can be null.</p>
     *
     * @param source ThirdMoment to copy
     * @param dest ThirdMoment to copy to
     * @throws NullArgumentException if either source or dest is null
     */
    public static void copy(ThirdMoment source, ThirdMoment dest)
        throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        SecondMoment.copy(source, dest);
        dest.m3 = source.m3;
        dest.nDevSq = source.nDevSq;
    }

}
