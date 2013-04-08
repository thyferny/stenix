package distribution;

import exception.NotStrictlyPositiveException;

public interface MultivariateRealDistribution {
    /**
     * Returns the probability density function (PDF) of this distribution
     * evaluated at the specified point {@code x}. In general, the PDF is the
     * derivative of the cumulative distribution function. If the derivative
     * does not exist at {@code x}, then an appropriate replacement should be
     * returned, e.g. {@code Double.POSITIVE_INFINITY}, {@code Double.NaN}, or
     * the limit inferior or limit superior of the difference quotient.
     *
     * @param x Point at which the PDF is evaluated.
     * @return the value of the probability density function at point {@code x}.
     */
    double density(double[] x);

    /**
     * Reseeds the random generator used to generate samples.
     *
     * @param seed Seed with which to initialize the random number generator.
     */
    void reseedRandomGenerator(long seed);

    /**
     * Gets the number of random variables of the distribution.
     * It is the size of the array returned by the {@link #sample() sample}
     * method.
     *
     * @return the number of variables.
     */
    int getDimension();

    /**
     * Generates a random value vector sampled from this distribution.
     *
     * @return a random value vector.
     */
    double[] sample();

    /**
     * Generates a list of a random value vectors from the distribution.
     *
     * @param sampleSize the number of random vectors to generate.
     * @return an array representing the random samples.
     * @throws org.apache.commons.math3.exception.NotStrictlyPositiveException
     * if {@code sampleSize} is not positive.
     *
     * @see #sample()
     */
    double[][] sample(int sampleSize) throws NotStrictlyPositiveException;
}
