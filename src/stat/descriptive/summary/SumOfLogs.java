package stat.descriptive.summary;

import java.io.Serializable;

import math.util.FastMath;
import math.util.MathUtils;
import stat.descriptive.AbstractStorelessUnivariateStatistic;
import exception.MathIllegalArgumentException;
import exception.NullArgumentException;

public class SumOfLogs extends AbstractStorelessUnivariateStatistic implements Serializable {

    /** Serializable version identifier */
    private static final long serialVersionUID = -370076995648386763L;

    /**Number of values that have been added */
    private int n;

    /**
     * The currently running value
     */
    private double value;

    /**
     * Create a SumOfLogs instance
     */
    public SumOfLogs() {
       value = 0d;
       n = 0;
    }

    /**
     * Copy constructor, creates a new {@code SumOfLogs} identical
     * to the {@code original}
     *
     * @param original the {@code SumOfLogs} instance to copy
     * @throws NullArgumentException if original is null
     */
    public SumOfLogs(SumOfLogs original) throws NullArgumentException {
        copy(original, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increment(final double d) {
        value += FastMath.log(d);
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
        value = 0d;
        n = 0;
    }

    /**
     * Returns the sum of the natural logs of the entries in the specified portion of
     * the input array, or <code>Double.NaN</code> if the designated subarray
     * is empty.
     * <p>
     * Throws <code>MathIllegalArgumentException</code> if the array is null.</p>
     * <p>
     * See {@link SumOfLogs}.</p>
     *
     * @param values the input array
     * @param begin index of the first array element to include
     * @param length the number of elements to include
     * @return the sum of the natural logs of the values or 0 if
     * length = 0
     * @throws MathIllegalArgumentException if the array is null or the array index
     *  parameters are not valid
     */
    @Override
    public double evaluate(final double[] values, final int begin, final int length)
    throws MathIllegalArgumentException {
        double sumLog = Double.NaN;
        if (test(values, begin, length, true)) {
            sumLog = 0.0;
            for (int i = begin; i < begin + length; i++) {
                sumLog += FastMath.log(values[i]);
            }
        }
        return sumLog;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SumOfLogs copy() {
        SumOfLogs result = new SumOfLogs();
        // No try-catch or advertised exception here because args are valid
        copy(this, result);
        return result;
    }

    /**
     * Copies source to dest.
     * <p>Neither source nor dest can be null.</p>
     *
     * @param source SumOfLogs to copy
     * @param dest SumOfLogs to copy to
     * @throws NullArgumentException if either source or dest is null
     */
    public static void copy(SumOfLogs source, SumOfLogs dest)
        throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        dest.setData(source.getDataRef());
        dest.n = source.n;
        dest.value = source.value;
    }
}
