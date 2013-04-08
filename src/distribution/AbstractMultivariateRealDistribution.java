package distribution;

import random.RandomGenerator;
import exception.NotStrictlyPositiveException;
import exception.util.LocalizedFormats;

public abstract class AbstractMultivariateRealDistribution
    implements MultivariateRealDistribution {
    /** RNG instance used to generate samples from the distribution. */
    protected final RandomGenerator random;
    /** The number of dimensions or columns in the multivariate distribution. */
    private final int dimension;

    /**
     * @param rng Random number generator.
     * @param n Number of dimensions.
     */
    protected AbstractMultivariateRealDistribution(RandomGenerator rng,
                                                   int n) {
        random = rng;
        dimension = n;
    }

    /** {@inheritDoc} */
    public void reseedRandomGenerator(long seed) {
        random.setSeed(seed);
    }

    /** {@inheritDoc} */
    public int getDimension() {
        return dimension;
    }

    /** {@inheritDoc} */
    public abstract double[] sample();

    /** {@inheritDoc} */
    public double[][] sample(final int sampleSize) {
        if (sampleSize <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES,
                                                   sampleSize);
        }
        final double[][] out = new double[sampleSize][dimension];
        for (int i = 0; i < sampleSize; i++) {
            out[i] = sample();
        }
        return out;
    }
}
