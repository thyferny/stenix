package stat.descriptive.summary;

import java.io.Serializable;

import math.util.FastMath;
import math.util.MathUtils;
import stat.descriptive.AbstractStorelessUnivariateStatistic;
import stat.descriptive.WeightedEvaluation;
import exception.MathIllegalArgumentException;
import exception.NullArgumentException;

public class Product extends AbstractStorelessUnivariateStatistic implements Serializable, WeightedEvaluation {

    /** Serializable version identifier */
    private static final long serialVersionUID = 2824226005990582538L;

    /**The number of values that have been added */
    private long n;

    /**
     * The current Running Product.
     */
    private double value;

    /**
     * Create a Product instance
     */
    public Product() {
        n = 0;
        value = 1;
    }

    /**
     * Copy constructor, creates a new {@code Product} identical
     * to the {@code original}
     *
     * @param original the {@code Product} instance to copy
     * @throws NullArgumentException  if original is null
     */
    public Product(Product original) throws NullArgumentException {
        copy(original, this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void increment(final double d) {
        value *= d;
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
        value = 1;
        n = 0;
    }

    /**
     * Returns the product of the entries in the specified portion of
     * the input array, or <code>Double.NaN</code> if the designated subarray
     * is empty.
     * <p>
     * Throws <code>MathIllegalArgumentException</code> if the array is null.</p>
     *
     * @param values the input array
     * @param begin index of the first array element to include
     * @param length the number of elements to include
     * @return the product of the values or 1 if length = 0
     * @throws MathIllegalArgumentException if the array is null or the array index
     *  parameters are not valid
     */
    @Override
    public double evaluate(final double[] values, final int begin, final int length)
    throws MathIllegalArgumentException {
        double product = Double.NaN;
        if (test(values, begin, length, true)) {
            product = 1.0;
            for (int i = begin; i < begin + length; i++) {
                product *= values[i];
            }
        }
        return product;
    }

    /**
     * <p>Returns the weighted product of the entries in the specified portion of
     * the input array, or <code>Double.NaN</code> if the designated subarray
     * is empty.</p>
     *
     * <p>Throws <code>MathIllegalArgumentException</code> if any of the following are true:
     * <ul><li>the values array is null</li>
     *     <li>the weights array is null</li>
     *     <li>the weights array does not have the same length as the values array</li>
     *     <li>the weights array contains one or more infinite values</li>
     *     <li>the weights array contains one or more NaN values</li>
     *     <li>the weights array contains negative values</li>
     *     <li>the start and length arguments do not determine a valid array</li>
     * </ul></p>
     *
     * <p>Uses the formula, <pre>
     *    weighted product = &prod;values[i]<sup>weights[i]</sup>
     * </pre>
     * that is, the weights are applied as exponents when computing the weighted product.</p>
     *
     * @param values the input array
     * @param weights the weights array
     * @param begin index of the first array element to include
     * @param length the number of elements to include
     * @return the product of the values or 1 if length = 0
     * @throws MathIllegalArgumentException if the parameters are not valid
     * @since 2.1
     */
    public double evaluate(final double[] values, final double[] weights,
        final int begin, final int length) throws MathIllegalArgumentException {
        double product = Double.NaN;
        if (test(values, weights, begin, length, true)) {
            product = 1.0;
            for (int i = begin; i < begin + length; i++) {
                product *= FastMath.pow(values[i], weights[i]);
            }
        }
        return product;
    }

    /**
     * <p>Returns the weighted product of the entries in the input array.</p>
     *
     * <p>Throws <code>MathIllegalArgumentException</code> if any of the following are true:
     * <ul><li>the values array is null</li>
     *     <li>the weights array is null</li>
     *     <li>the weights array does not have the same length as the values array</li>
     *     <li>the weights array contains one or more infinite values</li>
     *     <li>the weights array contains one or more NaN values</li>
     *     <li>the weights array contains negative values</li>
     * </ul></p>
     *
     * <p>Uses the formula, <pre>
     *    weighted product = &prod;values[i]<sup>weights[i]</sup>
     * </pre>
     * that is, the weights are applied as exponents when computing the weighted product.</p>
     *
     * @param values the input array
     * @param weights the weights array
     * @return the product of the values or Double.NaN if length = 0
     * @throws MathIllegalArgumentException if the parameters are not valid
     * @since 2.1
     */
    public double evaluate(final double[] values, final double[] weights)
    throws MathIllegalArgumentException {
        return evaluate(values, weights, 0, values.length);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Product copy() {
        Product result = new Product();
        // No try-catch or advertised exception because args are valid
        copy(this, result);
        return result;
    }

    /**
     * Copies source to dest.
     * <p>Neither source nor dest can be null.</p>
     *
     * @param source Product to copy
     * @param dest Product to copy to
     * @throws NullArgumentException if either source or dest is null
     */
    public static void copy(Product source, Product dest)
        throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        dest.setData(source.getDataRef());
        dest.n = source.n;
        dest.value = source.value;
    }

}
