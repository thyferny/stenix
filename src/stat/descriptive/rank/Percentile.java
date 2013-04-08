package stat.descriptive.rank;

import java.io.Serializable;
import java.util.Arrays;

import math.util.FastMath;
import math.util.MathUtils;
import stat.descriptive.AbstractUnivariateStatistic;
import exception.MathIllegalArgumentException;
import exception.NullArgumentException;
import exception.OutOfRangeException;
import exception.util.LocalizedFormats;

public class Percentile extends AbstractUnivariateStatistic implements Serializable {

    /** Serializable version identifier */
    private static final long serialVersionUID = -8091216485095130416L;

    /** Minimum size under which we use a simple insertion sort rather than Hoare's select. */
    private static final int MIN_SELECT_SIZE = 15;

    /** Maximum number of partitioning pivots cached (each level double the number of pivots). */
    private static final int MAX_CACHED_LEVELS = 10;

    /** Determines what percentile is computed when evaluate() is activated
     * with no quantile argument */
    private double quantile = 0.0;

    /** Cached pivots. */
    private int[] cachedPivots;

    /**
     * Constructs a Percentile with a default quantile
     * value of 50.0.
     */
    public Percentile() {
        // No try-catch or advertised exception here - arg is valid
        this(50.0);
    }

    /**
     * Constructs a Percentile with the specific quantile value.
     * @param p the quantile
     * @throws MathIllegalArgumentException  if p is not greater than 0 and less
     * than or equal to 100
     */
    public Percentile(final double p) throws MathIllegalArgumentException {
        setQuantile(p);
        cachedPivots = null;
    }

    /**
     * Copy constructor, creates a new {@code Percentile} identical
     * to the {@code original}
     *
     * @param original the {@code Percentile} instance to copy
     * @throws NullArgumentException if original is null
     */
    public Percentile(Percentile original) throws NullArgumentException {
        copy(original, this);
    }

    /** {@inheritDoc} */
    @Override
    public void setData(final double[] values) {
        if (values == null) {
            cachedPivots = null;
        } else {
            cachedPivots = new int[(0x1 << MAX_CACHED_LEVELS) - 1];
            Arrays.fill(cachedPivots, -1);
        }
        super.setData(values);
    }

    /** {@inheritDoc} */
    @Override
    public void setData(final double[] values, final int begin, final int length)
    throws MathIllegalArgumentException {
        if (values == null) {
            cachedPivots = null;
        } else {
            cachedPivots = new int[(0x1 << MAX_CACHED_LEVELS) - 1];
            Arrays.fill(cachedPivots, -1);
        }
        super.setData(values, begin, length);
    }

    /**
     * Returns the result of evaluating the statistic over the stored data.
     * <p>
     * The stored array is the one which was set by previous calls to
     * {@link #setData(double[])}
     * </p>
     * @param p the percentile value to compute
     * @return the value of the statistic applied to the stored data
     * @throws MathIllegalArgumentException if p is not a valid quantile value
     * (p must be greater than 0 and less than or equal to 100)
     */
    public double evaluate(final double p) throws MathIllegalArgumentException {
        return evaluate(getDataRef(), p);
    }

    /**
     * Returns an estimate of the <code>p</code>th percentile of the values
     * in the <code>values</code> array.
     * <p>
     * Calls to this method do not modify the internal <code>quantile</code>
     * state of this statistic.</p>
     * <p>
     * <ul>
     * <li>Returns <code>Double.NaN</code> if <code>values</code> has length
     * <code>0</code></li>
     * <li>Returns (for any value of <code>p</code>) <code>values[0]</code>
     *  if <code>values</code> has length <code>1</code></li>
     * <li>Throws <code>MathIllegalArgumentException</code> if <code>values</code>
     * is null or p is not a valid quantile value (p must be greater than 0
     * and less than or equal to 100) </li>
     * </ul></p>
     * <p>
     * See {@link Percentile} for a description of the percentile estimation
     * algorithm used.</p>
     *
     * @param values input array of values
     * @param p the percentile value to compute
     * @return the percentile value or Double.NaN if the array is empty
     * @throws MathIllegalArgumentException if <code>values</code> is null
     *     or p is invalid
     */
    public double evaluate(final double[] values, final double p)
    throws MathIllegalArgumentException {
        test(values, 0, 0);
        return evaluate(values, 0, values.length, p);
    }

    /**
     * Returns an estimate of the <code>quantile</code>th percentile of the
     * designated values in the <code>values</code> array.  The quantile
     * estimated is determined by the <code>quantile</code> property.
     * <p>
     * <ul>
     * <li>Returns <code>Double.NaN</code> if <code>length = 0</code></li>
     * <li>Returns (for any value of <code>quantile</code>)
     * <code>values[begin]</code> if <code>length = 1 </code></li>
     * <li>Throws <code>MathIllegalArgumentException</code> if <code>values</code>
     * is null, or <code>start</code> or <code>length</code> is invalid</li>
     * </ul></p>
     * <p>
     * See {@link Percentile} for a description of the percentile estimation
     * algorithm used.</p>
     *
     * @param values the input array
     * @param start index of the first array element to include
     * @param length the number of elements to include
     * @return the percentile value
     * @throws MathIllegalArgumentException if the parameters are not valid
     *
     */
    @Override
    public double evaluate(final double[] values, final int start, final int length)
    throws MathIllegalArgumentException {
        return evaluate(values, start, length, quantile);
    }

     /**
     * Returns an estimate of the <code>p</code>th percentile of the values
     * in the <code>values</code> array, starting with the element in (0-based)
     * position <code>begin</code> in the array and including <code>length</code>
     * values.
     * <p>
     * Calls to this method do not modify the internal <code>quantile</code>
     * state of this statistic.</p>
     * <p>
     * <ul>
     * <li>Returns <code>Double.NaN</code> if <code>length = 0</code></li>
     * <li>Returns (for any value of <code>p</code>) <code>values[begin]</code>
     *  if <code>length = 1 </code></li>
     * <li>Throws <code>MathIllegalArgumentException</code> if <code>values</code>
     *  is null , <code>begin</code> or <code>length</code> is invalid, or
     * <code>p</code> is not a valid quantile value (p must be greater than 0
     * and less than or equal to 100)</li>
     * </ul></p>
     * <p>
     * See {@link Percentile} for a description of the percentile estimation
     * algorithm used.</p>
     *
     * @param values array of input values
     * @param p  the percentile to compute
     * @param begin  the first (0-based) element to include in the computation
     * @param length  the number of array elements to include
     * @return  the percentile value
     * @throws MathIllegalArgumentException if the parameters are not valid or the
     * input array is null
     */
    public double evaluate(final double[] values, final int begin,
            final int length, final double p) throws MathIllegalArgumentException {

        test(values, begin, length);

        if ((p > 100) || (p <= 0)) {
            throw new OutOfRangeException(
                    LocalizedFormats.OUT_OF_BOUNDS_QUANTILE_VALUE, p, 0, 100);
        }
        if (length == 0) {
            return Double.NaN;
        }
        if (length == 1) {
            return values[begin]; // always return single value for n = 1
        }
        double n = length;
        double pos = p * (n + 1) / 100;
        double fpos = FastMath.floor(pos);
        int intPos = (int) fpos;
        double dif = pos - fpos;
        double[] work;
        int[] pivotsHeap;
        if (values == getDataRef()) {
            work = getDataRef();
            pivotsHeap = cachedPivots;
        } else {
            work = new double[length];
            System.arraycopy(values, begin, work, 0, length);
            pivotsHeap = new int[(0x1 << MAX_CACHED_LEVELS) - 1];
            Arrays.fill(pivotsHeap, -1);
        }

        if (pos < 1) {
            return select(work, pivotsHeap, 0);
        }
        if (pos >= n) {
            return select(work, pivotsHeap, length - 1);
        }
        double lower = select(work, pivotsHeap, intPos - 1);
        double upper = select(work, pivotsHeap, intPos);
        return lower + dif * (upper - lower);
    }

    /**
     * Select the k<sup>th</sup> smallest element from work array
     * @param work work array (will be reorganized during the call)
     * @param pivotsHeap set of pivot index corresponding to elements that
     * are already at their sorted location, stored as an implicit heap
     * (i.e. a sorted binary tree stored in a flat array, where the
     * children of a node at index n are at indices 2n+1 for the left
     * child and 2n+2 for the right child, with 0-based indices)
     * @param k index of the desired element
     * @return k<sup>th</sup> smallest element
     */
    private double select(final double[] work, final int[] pivotsHeap, final int k) {

        int begin = 0;
        int end   = work.length;
        int node  = 0;

        while (end - begin > MIN_SELECT_SIZE) {

            final int pivot;
            if ((node < pivotsHeap.length) && (pivotsHeap[node] >= 0)) {
                // the pivot has already been found in a previous call
                // and the array has already been partitioned around it
                pivot = pivotsHeap[node];
            } else {
                // select a pivot and partition work array around it
                pivot = partition(work, begin, end, medianOf3(work, begin, end));
                if (node < pivotsHeap.length) {
                    pivotsHeap[node] =  pivot;
                }
            }

            if (k == pivot) {
                // the pivot was exactly the element we wanted
                return work[k];
            } else if (k < pivot) {
                // the element is in the left partition
                end  = pivot;
                node = FastMath.min(2 * node + 1, pivotsHeap.length); // the min is here to avoid integer overflow
            } else {
                // the element is in the right partition
                begin = pivot + 1;
                node  = FastMath.min(2 * node + 2, pivotsHeap.length); // the min is here to avoid integer overflow
            }

        }

        // the element is somewhere in the small sub-array
        // sort the sub-array using insertion sort
        insertionSort(work, begin, end);
        return work[k];

    }

    /** Select a pivot index as the median of three
     * @param work data array
     * @param begin index of the first element of the slice
     * @param end index after the last element of the slice
     * @return the index of the median element chosen between the
     * first, the middle and the last element of the array slice
     */
    int medianOf3(final double[] work, final int begin, final int end) {

        final int inclusiveEnd = end - 1;
        final int    middle    = begin + (inclusiveEnd - begin) / 2;
        final double wBegin    = work[begin];
        final double wMiddle   = work[middle];
        final double wEnd      = work[inclusiveEnd];

        if (wBegin < wMiddle) {
            if (wMiddle < wEnd) {
                return middle;
            } else {
                return (wBegin < wEnd) ? inclusiveEnd : begin;
            }
        } else {
            if (wBegin < wEnd) {
                return begin;
            } else {
                return (wMiddle < wEnd) ? inclusiveEnd : middle;
            }
        }

    }

    /**
     * Partition an array slice around a pivot
     * <p>
     * Partitioning exchanges array elements such that all elements
     * smaller than pivot are before it and all elements larger than
     * pivot are after it
     * </p>
     * @param work data array
     * @param begin index of the first element of the slice
     * @param end index after the last element of the slice
     * @param pivot initial index of the pivot
     * @return index of the pivot after partition
     */
    private int partition(final double[] work, final int begin, final int end, final int pivot) {

        final double value = work[pivot];
        work[pivot] = work[begin];

        int i = begin + 1;
        int j = end - 1;
        while (i < j) {
            while ((i < j) && (work[j] > value)) {
                --j;
            }
            while ((i < j) && (work[i] < value)) {
                ++i;
            }

            if (i < j) {
                final double tmp = work[i];
                work[i++] = work[j];
                work[j--] = tmp;
            }
        }

        if ((i >= end) || (work[i] > value)) {
            --i;
        }
        work[begin] = work[i];
        work[i]     = value;
        return i;

    }

    /**
     * Sort in place a (small) array slice using insertion sort
     * @param work array to sort
     * @param begin index of the first element of the slice to sort
     * @param end index after the last element of the slice to sort
     */
    private void insertionSort(final double[] work, final int begin, final int end) {
        for (int j = begin + 1; j < end; j++) {
            final double saved = work[j];
            int i = j - 1;
            while ((i >= begin) && (saved < work[i])) {
                work[i + 1] = work[i];
                i--;
            }
            work[i + 1] = saved;
        }
    }

    /**
     * Returns the value of the quantile field (determines what percentile is
     * computed when evaluate() is called with no quantile argument).
     *
     * @return quantile
     */
    public double getQuantile() {
        return quantile;
    }

    /**
     * Sets the value of the quantile field (determines what percentile is
     * computed when evaluate() is called with no quantile argument).
     *
     * @param p a value between 0 < p <= 100
     * @throws MathIllegalArgumentException  if p is not greater than 0 and less
     * than or equal to 100
     */
    public void setQuantile(final double p) throws MathIllegalArgumentException {
        if (p <= 0 || p > 100) {
            throw new OutOfRangeException(
                    LocalizedFormats.OUT_OF_BOUNDS_QUANTILE_VALUE, p, 0, 100);
        }
        quantile = p;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Percentile copy() {
        Percentile result = new Percentile();
        //No try-catch or advertised exception because args are guaranteed non-null
        copy(this, result);
        return result;
    }

    /**
     * Copies source to dest.
     * <p>Neither source nor dest can be null.</p>
     *
     * @param source Percentile to copy
     * @param dest Percentile to copy to
     * @throws NullArgumentException if either source or dest is null
     */
    public static void copy(Percentile source, Percentile dest)
        throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        dest.setData(source.getDataRef());
        if (source.cachedPivots != null) {
            System.arraycopy(source.cachedPivots, 0, dest.cachedPivots, 0, source.cachedPivots.length);
        }
        dest.quantile = source.quantile;
    }

}
