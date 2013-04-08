package stat.descriptive.summary;

import java.io.Serializable;

import math.util.MathUtils;
import stat.descriptive.AbstractStorelessUnivariateStatistic;
import exception.MathIllegalArgumentException;
import exception.NullArgumentException;


public class SumOfSquares extends AbstractStorelessUnivariateStatistic implements Serializable {

    /** Serializable version identifier */
    private static final long serialVersionUID = 1460986908574398008L;

    /** */
    private long n;

    /**
     * The currently running sumSq
     */
    private double value;

    /**
     * Create a SumOfSquares instance
     */
    public SumOfSquares() {
        n = 0;
        value = 0;
    }

    /**
     * Copy constructor, creates a new {@code SumOfSquares} identical
     * to the {@code original}
     *
     * @param original the {@code SumOfSquares} instance to copy
     * @throws NullArgumentException if original is null
     */
    public SumOfSquares(SumOfSquares original) throws NullArgumentException {
        copy(original, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increment(final double d) {
        value += d * d;
        n++;
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
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        value = 0;
        n = 0;
    }

    /**
     * Returns the sum of the squares of the entries in the specified portion of
     * the input array, or <code>Double.NaN</code> if the designated subarray
     * is empty.
     * <p>
     * Throws <code>MathIllegalArgumentException</code> if the array is null.</p>
     *
     * @param values the input array
     * @param begin index of the first array element to include
     * @param length the number of elements to include
     * @return the sum of the squares of the values or 0 if length = 0
     * @throws MathIllegalArgumentException if the array is null or the array index
     *  parameters are not valid
     */
    @Override
    public double evaluate(final double[] values,final int begin, final int length)
    throws MathIllegalArgumentException {
        double sumSq = Double.NaN;
        if (test(values, begin, length, true)) {
            sumSq = 0.0;
            for (int i = begin; i < begin + length; i++) {
                sumSq += values[i] * values[i];
            }
        }
        return sumSq;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SumOfSquares copy() {
        SumOfSquares result = new SumOfSquares();
        // no try-catch or advertised exception here because args are valid
        copy(this, result);
        return result;
    }

    /**
     * Copies source to dest.
     * <p>Neither source nor dest can be null.</p>
     *
     * @param source SumOfSquares to copy
     * @param dest SumOfSquares to copy to
     * @throws NullArgumentException if either source or dest is null
     */
    public static void copy(SumOfSquares source, SumOfSquares dest)
        throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        dest.setData(source.getDataRef());
        dest.n = source.n;
        dest.value = source.value;
    }

}
