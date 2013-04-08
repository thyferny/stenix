package stat.descriptive.moment;

import java.io.Serializable;

import math.util.FastMath;
import math.util.MathUtils;
import stat.descriptive.AbstractStorelessUnivariateStatistic;
import stat.descriptive.StorelessUnivariateStatistic;
import stat.descriptive.summary.SumOfLogs;
import exception.MathIllegalArgumentException;
import exception.MathIllegalStateException;
import exception.NullArgumentException;
import exception.util.LocalizedFormats;


public class GeometricMean extends AbstractStorelessUnivariateStatistic implements Serializable {

    /** Serializable version identifier */
    private static final long serialVersionUID = -8178734905303459453L;

    /** Wrapped SumOfLogs instance */
    private StorelessUnivariateStatistic sumOfLogs;

    /**
     * Create a GeometricMean instance
     */
    public GeometricMean() {
        sumOfLogs = new SumOfLogs();
    }

    /**
     * Copy constructor, creates a new {@code GeometricMean} identical
     * to the {@code original}
     *
     * @param original the {@code GeometricMean} instance to copy
     * @throws NullArgumentException if original is null
     */
    public GeometricMean(GeometricMean original) throws NullArgumentException {
        super();
        copy(original, this);
    }

    /**
     * Create a GeometricMean instance using the given SumOfLogs instance
     * @param sumOfLogs sum of logs instance to use for computation
     */
    public GeometricMean(SumOfLogs sumOfLogs) {
        this.sumOfLogs = sumOfLogs;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GeometricMean copy() {
        GeometricMean result = new GeometricMean();
        // no try-catch or advertised exception because args guaranteed non-null
        copy(this, result);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increment(final double d) {
        sumOfLogs.increment(d);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getResult() {
        if (sumOfLogs.getN() > 0) {
            return FastMath.exp(sumOfLogs.getResult() / sumOfLogs.getN());
        } else {
            return Double.NaN;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        sumOfLogs.clear();
    }

    /**
     * Returns the geometric mean of the entries in the specified portion
     * of the input array.
     * <p>
     * See {@link GeometricMean} for details on the computing algorithm.</p>
     * <p>
     * Throws <code>IllegalArgumentException</code> if the array is null.</p>
     *
     * @param values input array containing the values
     * @param begin first array element to include
     * @param length the number of elements to include
     * @return the geometric mean or Double.NaN if length = 0 or
     * any of the values are &lt;= 0.
     * @throws MathIllegalArgumentException if the input array is null or the array
     * index parameters are not valid
     */
    @Override
    public double evaluate(
        final double[] values, final int begin, final int length)
    throws MathIllegalArgumentException {
        return FastMath.exp(
            sumOfLogs.evaluate(values, begin, length) / length);
    }

    /**
     * {@inheritDoc}
     */
    public long getN() {
        return sumOfLogs.getN();
    }

    /**
     * <p>Sets the implementation for the sum of logs.</p>
     * <p>This method must be activated before any data has been added - i.e.,
     * before {@link #increment(double) increment} has been used to add data;
     * otherwise an IllegalStateException will be thrown.</p>
     *
     * @param sumLogImpl the StorelessUnivariateStatistic instance to use
     * for computing the log sum
     * @throws MathIllegalStateException if data has already been added
     *  (i.e if n > 0)
     */
    public void setSumLogImpl(StorelessUnivariateStatistic sumLogImpl)
    throws MathIllegalStateException {
        checkEmpty();
        this.sumOfLogs = sumLogImpl;
    }

    /**
     * Returns the currently configured sum of logs implementation
     *
     * @return the StorelessUnivariateStatistic implementing the log sum
     */
    public StorelessUnivariateStatistic getSumLogImpl() {
        return sumOfLogs;
    }

    /**
     * Copies source to dest.
     * <p>Neither source nor dest can be null.</p>
     *
     * @param source GeometricMean to copy
     * @param dest GeometricMean to copy to
     * @throws NullArgumentException if either source or dest is null
     */
    public static void copy(GeometricMean source, GeometricMean dest)
        throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        dest.setData(source.getDataRef());
        dest.sumOfLogs = source.sumOfLogs.copy();
    }


    /**
     * Throws MathIllegalStateException if n > 0.
     * @throws MathIllegalStateException if data has been added to this statistic
     */
    private void checkEmpty() throws MathIllegalStateException {
        if (getN() > 0) {
            throw new MathIllegalStateException(
                    LocalizedFormats.VALUES_ADDED_BEFORE_CONFIGURING_STATISTIC,
                    getN());
        }
    }

}
