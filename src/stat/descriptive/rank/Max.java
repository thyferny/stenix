package stat.descriptive.rank;

import java.io.Serializable;

import math.util.MathUtils;
import stat.descriptive.AbstractStorelessUnivariateStatistic;
import exception.MathIllegalArgumentException;
import exception.NullArgumentException;

public class Max extends AbstractStorelessUnivariateStatistic implements Serializable {

    /** Serializable version identifier */
    private static final long serialVersionUID = -5593383832225844641L;

    /** Number of values that have been added */
    private long n;

    /** Current value of the statistic */
    private double value;

    /**
     * Create a Max instance
     */
    public Max() {
        n = 0;
        value = Double.NaN;
    }

    /**
     * Copy constructor, creates a new {@code Max} identical
     * to the {@code original}
     *
     * @param original the {@code Max} instance to copy
     * @throws NullArgumentException if original is null
     */
    public Max(Max original) throws NullArgumentException {
        copy(original, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increment(final double d) {
        if (d > value || Double.isNaN(value)) {
            value = d;
        }
        n++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        value = Double.NaN;
        n = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getResult() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    public long getN() {
        return n;
    }

    /**
     * Returns the maximum of the entries in the specified portion of
     * the input array, or <code>Double.NaN</code> if the designated subarray
     * is empty.
     * <p>
     * Throws <code>MathIllegalArgumentException</code> if the array is null or
     * the array index parameters are not valid.</p>
     * <p>
     * <ul>
     * <li>The result is <code>NaN</code> iff all values are <code>NaN</code>
     * (i.e. <code>NaN</code> values have no impact on the value of the statistic).</li>
     * <li>If any of the values equals <code>Double.POSITIVE_INFINITY</code>,
     * the result is <code>Double.POSITIVE_INFINITY.</code></li>
     * </ul></p>
     *
     * @param values the input array
     * @param begin index of the first array element to include
     * @param length the number of elements to include
     * @return the maximum of the values or Double.NaN if length = 0
     * @throws MathIllegalArgumentException if the array is null or the array index
     *  parameters are not valid
     */
    @Override
    public double evaluate(final double[] values, final int begin, final int length)
    throws MathIllegalArgumentException {
        double max = Double.NaN;
        if (test(values, begin, length)) {
            max = values[begin];
            for (int i = begin; i < begin + length; i++) {
                if (!Double.isNaN(values[i])) {
                    max = (max > values[i]) ? max : values[i];
                }
            }
        }
        return max;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Max copy() {
        Max result = new Max();
        // No try-catch or advertised exception because args are non-null
        copy(this, result);
        return result;
    }

    /**
     * Copies source to dest.
     * <p>Neither source nor dest can be null.</p>
     *
     * @param source Max to copy
     * @param dest Max to copy to
     * @throws NullArgumentException if either source or dest is null
     */
    public static void copy(Max source, Max dest)
        throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        dest.setData(source.getDataRef());
        dest.n = source.n;
        dest.value = source.value;
    }
}
