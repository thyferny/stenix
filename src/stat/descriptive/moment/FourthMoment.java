package stat.descriptive.moment;

import java.io.Serializable;

import math.util.MathUtils;
import exception.NullArgumentException;

class FourthMoment extends ThirdMoment implements Serializable{

    /** Serializable version identifier */
    private static final long serialVersionUID = 4763990447117157611L;

    /** fourth moment of values that have been added */
    private double m4;

    /**
     * Create a FourthMoment instance
     */
    public FourthMoment() {
        super();
        m4 = Double.NaN;
    }

    /**
     * Copy constructor, creates a new {@code FourthMoment} identical
     * to the {@code original}
     *
     * @param original the {@code FourthMoment} instance to copy
     * @throws NullArgumentException if original is null
     */
     public FourthMoment(FourthMoment original) throws NullArgumentException {
         super();
         copy(original, this);
     }

    /**
     * {@inheritDoc}
     */
     @Override
    public void increment(final double d) {
        if (n < 1) {
            m4 = 0.0;
            m3 = 0.0;
            m2 = 0.0;
            m1 = 0.0;
        }

        double prevM3 = m3;
        double prevM2 = m2;

        super.increment(d);

        double n0 = n;

        m4 = m4 - 4.0 * nDev * prevM3 + 6.0 * nDevSq * prevM2 +
            ((n0 * n0) - 3 * (n0 -1)) * (nDevSq * nDevSq * (n0 - 1) * n0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getResult() {
        return m4;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        super.clear();
        m4 = Double.NaN;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FourthMoment copy() {
        FourthMoment result = new FourthMoment();
        // No try-catch or advertised exception because args are guaranteed non-null
        copy(this, result);
        return result;
    }

    /**
     * Copies source to dest.
     * <p>Neither source nor dest can be null.</p>
     *
     * @param source FourthMoment to copy
     * @param dest FourthMoment to copy to
     * @throws NullArgumentException if either source or dest is null
     */
    public static void copy(FourthMoment source, FourthMoment dest)
        throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        ThirdMoment.copy(source, dest);
        dest.m4 = source.m4;
    }
}
