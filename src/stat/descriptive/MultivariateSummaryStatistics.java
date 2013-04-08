package stat.descriptive;

import java.io.Serializable;
import java.util.Arrays;

import linear.RealMatrix;
import math.util.FastMath;
import math.util.MathArrays;
import math.util.MathUtils;
import math.util.Precision;
import stat.descriptive.moment.GeometricMean;
import stat.descriptive.moment.Mean;
import stat.descriptive.moment.VectorialCovariance;
import stat.descriptive.rank.Max;
import stat.descriptive.rank.Min;
import stat.descriptive.summary.Sum;
import stat.descriptive.summary.SumOfLogs;
import stat.descriptive.summary.SumOfSquares;
import exception.DimensionMismatchException;
import exception.MathIllegalStateException;
import exception.util.LocalizedFormats;


public class MultivariateSummaryStatistics
    implements StatisticalMultivariateSummary, Serializable {

    /** Serialization UID */
    private static final long serialVersionUID = 2271900808994826718L;

    /** Dimension of the data. */
    private int k;

    /** Count of values that have been added */
    private long n = 0;

    /** Sum statistic implementation - can be reset by setter. */
    private StorelessUnivariateStatistic[] sumImpl;

    /** Sum of squares statistic implementation - can be reset by setter. */
    private StorelessUnivariateStatistic[] sumSqImpl;

    /** Minimum statistic implementation - can be reset by setter. */
    private StorelessUnivariateStatistic[] minImpl;

    /** Maximum statistic implementation - can be reset by setter. */
    private StorelessUnivariateStatistic[] maxImpl;

    /** Sum of log statistic implementation - can be reset by setter. */
    private StorelessUnivariateStatistic[] sumLogImpl;

    /** Geometric mean statistic implementation - can be reset by setter. */
    private StorelessUnivariateStatistic[] geoMeanImpl;

    /** Mean statistic implementation - can be reset by setter. */
    private StorelessUnivariateStatistic[] meanImpl;

    /** Covariance statistic implementation - cannot be reset. */
    private VectorialCovariance covarianceImpl;

    /**
     * Construct a MultivariateSummaryStatistics instance
     * @param k dimension of the data
     * @param isCovarianceBiasCorrected if true, the unbiased sample
     * covariance is computed, otherwise the biased population covariance
     * is computed
     */
    public MultivariateSummaryStatistics(int k, boolean isCovarianceBiasCorrected) {
        this.k = k;

        sumImpl     = new StorelessUnivariateStatistic[k];
        sumSqImpl   = new StorelessUnivariateStatistic[k];
        minImpl     = new StorelessUnivariateStatistic[k];
        maxImpl     = new StorelessUnivariateStatistic[k];
        sumLogImpl  = new StorelessUnivariateStatistic[k];
        geoMeanImpl = new StorelessUnivariateStatistic[k];
        meanImpl    = new StorelessUnivariateStatistic[k];

        for (int i = 0; i < k; ++i) {
            sumImpl[i]     = new Sum();
            sumSqImpl[i]   = new SumOfSquares();
            minImpl[i]     = new Min();
            maxImpl[i]     = new Max();
            sumLogImpl[i]  = new SumOfLogs();
            geoMeanImpl[i] = new GeometricMean();
            meanImpl[i]    = new Mean();
        }

        covarianceImpl =
            new VectorialCovariance(k, isCovarianceBiasCorrected);

    }

    /**
     * Add an n-tuple to the data
     *
     * @param value  the n-tuple to add
     * @throws DimensionMismatchException if the length of the array
     * does not match the one used at construction
     */
    public void addValue(double[] value) throws DimensionMismatchException {
        checkDimension(value.length);
        for (int i = 0; i < k; ++i) {
            double v = value[i];
            sumImpl[i].increment(v);
            sumSqImpl[i].increment(v);
            minImpl[i].increment(v);
            maxImpl[i].increment(v);
            sumLogImpl[i].increment(v);
            geoMeanImpl[i].increment(v);
            meanImpl[i].increment(v);
        }
        covarianceImpl.increment(value);
        n++;
    }

    /**
     * Returns the dimension of the data
     * @return The dimension of the data
     */
    public int getDimension() {
        return k;
    }

    /**
     * Returns the number of available values
     * @return The number of available values
     */
    public long getN() {
        return n;
    }

    /**
     * Returns an array of the results of a statistic.
     * @param stats univariate statistic array
     * @return results array
     */
    private double[] getResults(StorelessUnivariateStatistic[] stats) {
        double[] results = new double[stats.length];
        for (int i = 0; i < results.length; ++i) {
            results[i] = stats[i].getResult();
        }
        return results;
    }

    /**
     * Returns an array whose i<sup>th</sup> entry is the sum of the
     * i<sup>th</sup> entries of the arrays that have been added using
     * {@link #addValue(double[])}
     *
     * @return the array of component sums
     */
    public double[] getSum() {
        return getResults(sumImpl);
    }

    /**
     * Returns an array whose i<sup>th</sup> entry is the sum of squares of the
     * i<sup>th</sup> entries of the arrays that have been added using
     * {@link #addValue(double[])}
     *
     * @return the array of component sums of squares
     */
    public double[] getSumSq() {
        return getResults(sumSqImpl);
    }

    /**
     * Returns an array whose i<sup>th</sup> entry is the sum of logs of the
     * i<sup>th</sup> entries of the arrays that have been added using
     * {@link #addValue(double[])}
     *
     * @return the array of component log sums
     */
    public double[] getSumLog() {
        return getResults(sumLogImpl);
    }

    /**
     * Returns an array whose i<sup>th</sup> entry is the mean of the
     * i<sup>th</sup> entries of the arrays that have been added using
     * {@link #addValue(double[])}
     *
     * @return the array of component means
     */
    public double[] getMean() {
        return getResults(meanImpl);
    }

    /**
     * Returns an array whose i<sup>th</sup> entry is the standard deviation of the
     * i<sup>th</sup> entries of the arrays that have been added using
     * {@link #addValue(double[])}
     *
     * @return the array of component standard deviations
     */
    public double[] getStandardDeviation() {
        double[] stdDev = new double[k];
        if (getN() < 1) {
            Arrays.fill(stdDev, Double.NaN);
        } else if (getN() < 2) {
            Arrays.fill(stdDev, 0.0);
        } else {
            RealMatrix matrix = covarianceImpl.getResult();
            for (int i = 0; i < k; ++i) {
                stdDev[i] = FastMath.sqrt(matrix.getEntry(i, i));
            }
        }
        return stdDev;
    }

    /**
     * Returns the covariance matrix of the values that have been added.
     *
     * @return the covariance matrix
     */
    public RealMatrix getCovariance() {
        return covarianceImpl.getResult();
    }

    /**
     * Returns an array whose i<sup>th</sup> entry is the maximum of the
     * i<sup>th</sup> entries of the arrays that have been added using
     * {@link #addValue(double[])}
     *
     * @return the array of component maxima
     */
    public double[] getMax() {
        return getResults(maxImpl);
    }

    /**
     * Returns an array whose i<sup>th</sup> entry is the minimum of the
     * i<sup>th</sup> entries of the arrays that have been added using
     * {@link #addValue(double[])}
     *
     * @return the array of component minima
     */
    public double[] getMin() {
        return getResults(minImpl);
    }

    /**
     * Returns an array whose i<sup>th</sup> entry is the geometric mean of the
     * i<sup>th</sup> entries of the arrays that have been added using
     * {@link #addValue(double[])}
     *
     * @return the array of component geometric means
     */
    public double[] getGeometricMean() {
        return getResults(geoMeanImpl);
    }

    /**
     * Generates a text report displaying
     * summary statistics from values that
     * have been added.
     * @return String with line feeds displaying statistics
     */
    @Override
    public String toString() {
        final String separator = ", ";
        final String suffix = System.getProperty("line.separator");
        StringBuilder outBuffer = new StringBuilder();
        outBuffer.append("MultivariateSummaryStatistics:" + suffix);
        outBuffer.append("n: " + getN() + suffix);
        append(outBuffer, getMin(), "min: ", separator, suffix);
        append(outBuffer, getMax(), "max: ", separator, suffix);
        append(outBuffer, getMean(), "mean: ", separator, suffix);
        append(outBuffer, getGeometricMean(), "geometric mean: ", separator, suffix);
        append(outBuffer, getSumSq(), "sum of squares: ", separator, suffix);
        append(outBuffer, getSumLog(), "sum of logarithms: ", separator, suffix);
        append(outBuffer, getStandardDeviation(), "standard deviation: ", separator, suffix);
        outBuffer.append("covariance: " + getCovariance().toString() + suffix);
        return outBuffer.toString();
    }

    /**
     * Append a text representation of an array to a buffer.
     * @param buffer buffer to fill
     * @param data data array
     * @param prefix text prefix
     * @param separator elements separator
     * @param suffix text suffix
     */
    private void append(StringBuilder buffer, double[] data,
                        String prefix, String separator, String suffix) {
        buffer.append(prefix);
        for (int i = 0; i < data.length; ++i) {
            if (i > 0) {
                buffer.append(separator);
            }
            buffer.append(data[i]);
        }
        buffer.append(suffix);
    }

    /**
     * Resets all statistics and storage
     */
    public void clear() {
        this.n = 0;
        for (int i = 0; i < k; ++i) {
            minImpl[i].clear();
            maxImpl[i].clear();
            sumImpl[i].clear();
            sumLogImpl[i].clear();
            sumSqImpl[i].clear();
            geoMeanImpl[i].clear();
            meanImpl[i].clear();
        }
        covarianceImpl.clear();
    }

    /**
     * Returns true iff <code>object</code> is a <code>MultivariateSummaryStatistics</code>
     * instance and all statistics have the same values as this.
     * @param object the object to test equality against.
     * @return true if object equals this
     */
    @Override
    public boolean equals(Object object) {
        if (object == this ) {
            return true;
        }
        if (object instanceof MultivariateSummaryStatistics == false) {
            return false;
        }
        MultivariateSummaryStatistics stat = (MultivariateSummaryStatistics) object;
        return MathArrays.equalsIncludingNaN(stat.getGeometricMean(), getGeometricMean()) &&
               MathArrays.equalsIncludingNaN(stat.getMax(),           getMax())           &&
               MathArrays.equalsIncludingNaN(stat.getMean(),          getMean())          &&
               MathArrays.equalsIncludingNaN(stat.getMin(),           getMin())           &&
               Precision.equalsIncludingNaN(stat.getN(),             getN())             &&
               MathArrays.equalsIncludingNaN(stat.getSum(),           getSum())           &&
               MathArrays.equalsIncludingNaN(stat.getSumSq(),         getSumSq())         &&
               MathArrays.equalsIncludingNaN(stat.getSumLog(),        getSumLog())        &&
               stat.getCovariance().equals( getCovariance());
    }

    /**
     * Returns hash code based on values of statistics
     *
     * @return hash code
     */
    @Override
    public int hashCode() {
        int result = 31 + MathUtils.hash(getGeometricMean());
        result = result * 31 + MathUtils.hash(getGeometricMean());
        result = result * 31 + MathUtils.hash(getMax());
        result = result * 31 + MathUtils.hash(getMean());
        result = result * 31 + MathUtils.hash(getMin());
        result = result * 31 + MathUtils.hash(getN());
        result = result * 31 + MathUtils.hash(getSum());
        result = result * 31 + MathUtils.hash(getSumSq());
        result = result * 31 + MathUtils.hash(getSumLog());
        result = result * 31 + getCovariance().hashCode();
        return result;
    }

    // Getters and setters for statistics implementations
    /**
     * Sets statistics implementations.
     * @param newImpl new implementations for statistics
     * @param oldImpl old implementations for statistics
     * @throws DimensionMismatchException if the array dimension
     * does not match the one used at construction
     * @throws MathIllegalStateException if data has already been added
     * (i.e. if n > 0)
     */
    private void setImpl(StorelessUnivariateStatistic[] newImpl,
                         StorelessUnivariateStatistic[] oldImpl) throws MathIllegalStateException,
                         DimensionMismatchException {
        checkEmpty();
        checkDimension(newImpl.length);
        System.arraycopy(newImpl, 0, oldImpl, 0, newImpl.length);
    }

    /**
     * Returns the currently configured Sum implementation
     *
     * @return the StorelessUnivariateStatistic implementing the sum
     */
    public StorelessUnivariateStatistic[] getSumImpl() {
        return sumImpl.clone();
    }

    /**
     * <p>Sets the implementation for the Sum.</p>
     * <p>This method must be activated before any data has been added - i.e.,
     * before {@link #addValue(double[]) addValue} has been used to add data;
     * otherwise an IllegalStateException will be thrown.</p>
     *
     * @param sumImpl the StorelessUnivariateStatistic instance to use
     * for computing the Sum
     * @throws DimensionMismatchException if the array dimension
     * does not match the one used at construction
     * @throws MathIllegalStateException if data has already been added
     *  (i.e if n > 0)
     */
    public void setSumImpl(StorelessUnivariateStatistic[] sumImpl)
    throws MathIllegalStateException, DimensionMismatchException {
        setImpl(sumImpl, this.sumImpl);
    }

    /**
     * Returns the currently configured sum of squares implementation
     *
     * @return the StorelessUnivariateStatistic implementing the sum of squares
     */
    public StorelessUnivariateStatistic[] getSumsqImpl() {
        return sumSqImpl.clone();
    }

    /**
     * <p>Sets the implementation for the sum of squares.</p>
     * <p>This method must be activated before any data has been added - i.e.,
     * before {@link #addValue(double[]) addValue} has been used to add data;
     * otherwise an IllegalStateException will be thrown.</p>
     *
     * @param sumsqImpl the StorelessUnivariateStatistic instance to use
     * for computing the sum of squares
     * @throws DimensionMismatchException if the array dimension
     * does not match the one used at construction
     * @throws MathIllegalStateException if data has already been added
     *  (i.e if n > 0)
     */
    public void setSumsqImpl(StorelessUnivariateStatistic[] sumsqImpl)
    throws MathIllegalStateException, DimensionMismatchException {
        setImpl(sumsqImpl, this.sumSqImpl);
    }

    /**
     * Returns the currently configured minimum implementation
     *
     * @return the StorelessUnivariateStatistic implementing the minimum
     */
    public StorelessUnivariateStatistic[] getMinImpl() {
        return minImpl.clone();
    }

    /**
     * <p>Sets the implementation for the minimum.</p>
     * <p>This method must be activated before any data has been added - i.e.,
     * before {@link #addValue(double[]) addValue} has been used to add data;
     * otherwise an IllegalStateException will be thrown.</p>
     *
     * @param minImpl the StorelessUnivariateStatistic instance to use
     * for computing the minimum
     * @throws DimensionMismatchException if the array dimension
     * does not match the one used at construction
     * @throws MathIllegalStateException if data has already been added
     *  (i.e if n > 0)
     */
    public void setMinImpl(StorelessUnivariateStatistic[] minImpl)
    throws MathIllegalStateException, DimensionMismatchException {
        setImpl(minImpl, this.minImpl);
    }

    /**
     * Returns the currently configured maximum implementation
     *
     * @return the StorelessUnivariateStatistic implementing the maximum
     */
    public StorelessUnivariateStatistic[] getMaxImpl() {
        return maxImpl.clone();
    }

    /**
     * <p>Sets the implementation for the maximum.</p>
     * <p>This method must be activated before any data has been added - i.e.,
     * before {@link #addValue(double[]) addValue} has been used to add data;
     * otherwise an IllegalStateException will be thrown.</p>
     *
     * @param maxImpl the StorelessUnivariateStatistic instance to use
     * for computing the maximum
     * @throws DimensionMismatchException if the array dimension
     * does not match the one used at construction
     * @throws MathIllegalStateException if data has already been added
     *  (i.e if n > 0)
     */
    public void setMaxImpl(StorelessUnivariateStatistic[] maxImpl)
    throws MathIllegalStateException, DimensionMismatchException{
        setImpl(maxImpl, this.maxImpl);
    }

    /**
     * Returns the currently configured sum of logs implementation
     *
     * @return the StorelessUnivariateStatistic implementing the log sum
     */
    public StorelessUnivariateStatistic[] getSumLogImpl() {
        return sumLogImpl.clone();
    }

    /**
     * <p>Sets the implementation for the sum of logs.</p>
     * <p>This method must be activated before any data has been added - i.e.,
     * before {@link #addValue(double[]) addValue} has been used to add data;
     * otherwise an IllegalStateException will be thrown.</p>
     *
     * @param sumLogImpl the StorelessUnivariateStatistic instance to use
     * for computing the log sum
     * @throws DimensionMismatchException if the array dimension
     * does not match the one used at construction
     * @throws MathIllegalStateException if data has already been added
     *  (i.e if n > 0)
     */
    public void setSumLogImpl(StorelessUnivariateStatistic[] sumLogImpl)
    throws MathIllegalStateException, DimensionMismatchException{
        setImpl(sumLogImpl, this.sumLogImpl);
    }

    /**
     * Returns the currently configured geometric mean implementation
     *
     * @return the StorelessUnivariateStatistic implementing the geometric mean
     */
    public StorelessUnivariateStatistic[] getGeoMeanImpl() {
        return geoMeanImpl.clone();
    }

    /**
     * <p>Sets the implementation for the geometric mean.</p>
     * <p>This method must be activated before any data has been added - i.e.,
     * before {@link #addValue(double[]) addValue} has been used to add data;
     * otherwise an IllegalStateException will be thrown.</p>
     *
     * @param geoMeanImpl the StorelessUnivariateStatistic instance to use
     * for computing the geometric mean
     * @throws DimensionMismatchException if the array dimension
     * does not match the one used at construction
     * @throws MathIllegalStateException if data has already been added
     *  (i.e if n > 0)
     */
    public void setGeoMeanImpl(StorelessUnivariateStatistic[] geoMeanImpl)
    throws MathIllegalStateException, DimensionMismatchException {
        setImpl(geoMeanImpl, this.geoMeanImpl);
    }

    /**
     * Returns the currently configured mean implementation
     *
     * @return the StorelessUnivariateStatistic implementing the mean
     */
    public StorelessUnivariateStatistic[] getMeanImpl() {
        return meanImpl.clone();
    }

    /**
     * <p>Sets the implementation for the mean.</p>
     * <p>This method must be activated before any data has been added - i.e.,
     * before {@link #addValue(double[]) addValue} has been used to add data;
     * otherwise an IllegalStateException will be thrown.</p>
     *
     * @param meanImpl the StorelessUnivariateStatistic instance to use
     * for computing the mean
     * @throws DimensionMismatchException if the array dimension
     * does not match the one used at construction
     * @throws MathIllegalStateException if data has already been added
     *  (i.e if n > 0)
     */
    public void setMeanImpl(StorelessUnivariateStatistic[] meanImpl)
    throws MathIllegalStateException, DimensionMismatchException{
        setImpl(meanImpl, this.meanImpl);
    }

    /**
     * Throws MathIllegalStateException if the statistic is not empty.
     * @throws MathIllegalStateException if n > 0.
     */
    private void checkEmpty() throws MathIllegalStateException {
        if (n > 0) {
            throw new MathIllegalStateException(
                    LocalizedFormats.VALUES_ADDED_BEFORE_CONFIGURING_STATISTIC, n);
        }
    }

    /**
     * Throws DimensionMismatchException if dimension != k.
     * @param dimension dimension to check
     * @throws DimensionMismatchException if dimension != k
     */
    private void checkDimension(int dimension) throws DimensionMismatchException {
        if (dimension != k) {
            throw new DimensionMismatchException(dimension, k);
        }
    }
}
