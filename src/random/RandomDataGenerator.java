package random;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Collection;

import math.util.FastMath;
import distribution.BetaDistribution;
import distribution.BinomialDistribution;
import distribution.CauchyDistribution;
import distribution.ChiSquaredDistribution;
import distribution.ExponentialDistribution;
import distribution.FDistribution;
import distribution.GammaDistribution;
import distribution.HypergeometricDistribution;
import distribution.PascalDistribution;
import distribution.PoissonDistribution;
import distribution.TDistribution;
import distribution.WeibullDistribution;
import distribution.ZipfDistribution;
import exception.MathInternalError;
import exception.NotANumberException;
import exception.NotFiniteNumberException;
import exception.NotPositiveException;
import exception.NotStrictlyPositiveException;
import exception.NumberIsTooLargeException;
import exception.OutOfRangeException;
import exception.util.LocalizedFormats;

public class RandomDataGenerator implements RandomData, Serializable {

    /** Serializable version identifier */
    private static final long serialVersionUID = -626730818244969716L;

    /** underlying random number generator */
    private RandomGenerator rand = null;

    /** underlying secure random number generator */
    private SecureRandom secRand = null;

    /**
     * Construct a RandomDataGenerator, using a default random generator as the source
     * of randomness.
     *
     * <p>The default generator is a {@link Well19937c} seeded
     * with {@code System.currentTimeMillis() + System.identityHashCode(this))}.
     * The generator is initialized and seeded on first use.</p>
     */
    public RandomDataGenerator() {
    }

    /**
     * Construct a RandomDataGenerator using the supplied {@link RandomGenerator} as
     * the source of (non-secure) random data.
     *
     * @param rand the source of (non-secure) random data
     * (may be null, resulting in the default generator)
     */
    public RandomDataGenerator(RandomGenerator rand) {
        this.rand = rand;
    }

    /**
     * {@inheritDoc}
     * <p>
     * <strong>Algorithm Description:</strong> hex strings are generated using a
     * 2-step process.
     * <ol>
     * <li>{@code len / 2 + 1} binary bytes are generated using the underlying
     * Random</li>
     * <li>Each binary byte is translated into 2 hex digits</li>
     * </ol>
     * </p>
     *
     * @param len the desired string length.
     * @return the random string.
     * @throws NotStrictlyPositiveException if {@code len <= 0}.
     */
    public String nextHexString(int len) throws NotStrictlyPositiveException {
        if (len <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.LENGTH, len);
        }

        // Get a random number generator
        RandomGenerator ran = getRan();

        // Initialize output buffer
        StringBuilder outBuffer = new StringBuilder();

        // Get int(len/2)+1 random bytes
        byte[] randomBytes = new byte[(len / 2) + 1];
        ran.nextBytes(randomBytes);

        // Convert each byte to 2 hex digits
        for (int i = 0; i < randomBytes.length; i++) {
            Integer c = Integer.valueOf(randomBytes[i]);

            /*
             * Add 128 to byte value to make interval 0-255 before doing hex
             * conversion. This guarantees <= 2 hex digits from toHexString()
             * toHexString would otherwise add 2^32 to negative arguments.
             */
            String hex = Integer.toHexString(c.intValue() + 128);

            // Make sure we add 2 hex digits for each byte
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            outBuffer.append(hex);
        }
        return outBuffer.toString().substring(0, len);
    }

    /** {@inheritDoc} */
    public int nextInt(int lower, int upper) throws NumberIsTooLargeException {
        if (lower >= upper) {
            throw new NumberIsTooLargeException(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND,
                                                lower, upper, false);
        }
        double r = getRan().nextDouble();
        double scaled = r * upper + (1.0 - r) * lower + r;
        return (int) FastMath.floor(scaled);
    }

    /** {@inheritDoc} */
    public long nextLong(long lower, long upper) throws NumberIsTooLargeException {
        if (lower >= upper) {
            throw new NumberIsTooLargeException(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND,
                                                lower, upper, false);
        }
        double r = getRan().nextDouble();
        double scaled = r * upper + (1.0 - r) * lower + r;
        return (long)FastMath.floor(scaled);
    }

    /**
     * {@inheritDoc}
     * <p>
     * <strong>Algorithm Description:</strong> hex strings are generated in
     * 40-byte segments using a 3-step process.
     * <ol>
     * <li>
     * 20 random bytes are generated using the underlying
     * <code>SecureRandom</code>.</li>
     * <li>
     * SHA-1 hash is applied to yield a 20-byte binary digest.</li>
     * <li>
     * Each byte of the binary digest is converted to 2 hex digits.</li>
     * </ol>
     * </p>
     * @throws NotStrictlyPositiveException if {@code len <= 0}
     */
    public String nextSecureHexString(int len) throws NotStrictlyPositiveException {
        if (len <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.LENGTH, len);
        }

        // Get SecureRandom and setup Digest provider
        SecureRandom secRan = getSecRan();
        MessageDigest alg = null;
        try {
            alg = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException ex) {
            // this should never happen
            throw new MathInternalError(ex);
        }
        alg.reset();

        // Compute number of iterations required (40 bytes each)
        int numIter = (len / 40) + 1;

        StringBuilder outBuffer = new StringBuilder();
        for (int iter = 1; iter < numIter + 1; iter++) {
            byte[] randomBytes = new byte[40];
            secRan.nextBytes(randomBytes);
            alg.update(randomBytes);

            // Compute hash -- will create 20-byte binary hash
            byte[] hash = alg.digest();

            // Loop over the hash, converting each byte to 2 hex digits
            for (int i = 0; i < hash.length; i++) {
                Integer c = Integer.valueOf(hash[i]);

                /*
                 * Add 128 to byte value to make interval 0-255 This guarantees
                 * <= 2 hex digits from toHexString() toHexString would
                 * otherwise add 2^32 to negative arguments
                 */
                String hex = Integer.toHexString(c.intValue() + 128);

                // Keep strings uniform length -- guarantees 40 bytes
                if (hex.length() == 1) {
                    hex = "0" + hex;
                }
                outBuffer.append(hex);
            }
        }
        return outBuffer.toString().substring(0, len);
    }

    /**  {@inheritDoc} */
    public int nextSecureInt(int lower, int upper) throws NumberIsTooLargeException {
        if (lower >= upper) {
            throw new NumberIsTooLargeException(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND,
                                                lower, upper, false);
        }
        SecureRandom sec = getSecRan();
        final double r = sec.nextDouble();
        final double scaled = r * upper + (1.0 - r) * lower + r;
        return (int)FastMath.floor(scaled);
    }

    /** {@inheritDoc} */
    public long nextSecureLong(long lower, long upper) throws NumberIsTooLargeException {
        if (lower >= upper) {
            throw new NumberIsTooLargeException(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND,
                                                lower, upper, false);
        }
        SecureRandom sec = getSecRan();
        final double r = sec.nextDouble();
        final double scaled = r * upper + (1.0 - r) * lower + r;
        return (long)FastMath.floor(scaled);
    }

    /**
     * {@inheritDoc}
     * <p>
     * <strong>Algorithm Description</strong>:
     * <ul><li> For small means, uses simulation of a Poisson process
     * using Uniform deviates, as described
     * <a href="http://irmi.epfl.ch/cmos/Pmmi/interactive/rng7.htm"> here.</a>
     * The Poisson process (and hence value returned) is bounded by 1000 * mean.</li>
     *
     * <li> For large means, uses the rejection algorithm described in <br/>
     * Devroye, Luc. (1981).<i>The Computer Generation of Poisson Random Variables</i>
     * <strong>Computing</strong> vol. 26 pp. 197-207.</li></ul></p>
     * @throws NotStrictlyPositiveException if {@code len <= 0}
     */
    public long nextPoisson(double mean) throws NotStrictlyPositiveException {
        return new PoissonDistribution(getRan(), mean,
                PoissonDistribution.DEFAULT_EPSILON,
                PoissonDistribution.DEFAULT_MAX_ITERATIONS).sample();
    }

    /** {@inheritDoc} */
    public double nextGaussian(double mu, double sigma) throws NotStrictlyPositiveException {
        if (sigma <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.STANDARD_DEVIATION, sigma);
        }
        return sigma * getRan().nextGaussian() + mu;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * <strong>Algorithm Description</strong>: Uses the Algorithm SA (Ahrens)
     * from p. 876 in:
     * [1]: Ahrens, J. H. and Dieter, U. (1972). Computer methods for
     * sampling from the exponential and normal distributions.
     * Communications of the ACM, 15, 873-882.
     * </p>
     */
    public double nextExponential(double mean) throws NotStrictlyPositiveException {
        return new ExponentialDistribution(getRan(), mean,
                ExponentialDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY).sample();
    }

    /**
     * <p>Generates a random value from the
     * {@link org.apache.commons.math3.distribution.GammaDistribution Gamma Distribution}.</p>
     *
     * <p>This implementation uses the following algorithms: </p>
     *
     * <p>For 0 < shape < 1: <br/>
     * Ahrens, J. H. and Dieter, U., <i>Computer methods for
     * sampling from gamma, beta, Poisson and binomial distributions.</i>
     * Computing, 12, 223-246, 1974.</p>
     *
     * <p>For shape >= 1: <br/>
     * Marsaglia and Tsang, <i>A Simple Method for Generating
     * Gamma Variables.</i> ACM Transactions on Mathematical Software,
     * Volume 26 Issue 3, September, 2000.</p>
     *
     * @param shape the median of the Gamma distribution
     * @param scale the scale parameter of the Gamma distribution
     * @return random value sampled from the Gamma(shape, scale) distribution
     * @throws NotStrictlyPositiveException if {@code shape <= 0} or
     * {@code scale <= 0}.
     */
    public double nextGamma(double shape, double scale) throws NotStrictlyPositiveException {
        return new GammaDistribution(getRan(),shape, scale,
                GammaDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY).sample();
    }

    /**
     * Generates a random value from the {@link HypergeometricDistribution Hypergeometric Distribution}.
     *
     * @param populationSize the population size of the Hypergeometric distribution
     * @param numberOfSuccesses number of successes in the population of the Hypergeometric distribution
     * @param sampleSize the sample size of the Hypergeometric distribution
     * @return random value sampled from the Hypergeometric(numberOfSuccesses, sampleSize) distribution
     * @throws NumberIsTooLargeException  if {@code numberOfSuccesses > populationSize},
     * or {@code sampleSize > populationSize}.
     * @throws NotStrictlyPositiveException if {@code populationSize <= 0}.
     * @throws NotPositiveException  if {@code numberOfSuccesses < 0}.
     */
    public int nextHypergeometric(int populationSize, int numberOfSuccesses, int sampleSize) throws NotPositiveException, NotStrictlyPositiveException, NumberIsTooLargeException {
        return new HypergeometricDistribution(getRan(),populationSize,
                numberOfSuccesses, sampleSize).sample();
    }

    /**
     * Generates a random value from the {@link PascalDistribution Pascal Distribution}.
     *
     * @param r the number of successes of the Pascal distribution
     * @param p the probability of success of the Pascal distribution
     * @return random value sampled from the Pascal(r, p) distribution
     * @throws NotStrictlyPositiveException if the number of successes is not positive
     * @throws OutOfRangeException if the probability of success is not in the
     * range {@code [0, 1]}.
     */
    public int nextPascal(int r, double p) throws NotStrictlyPositiveException, OutOfRangeException {
        return new PascalDistribution(getRan(), r, p).sample();
    }

    /**
     * Generates a random value from the {@link TDistribution T Distribution}.
     *
     * @param df the degrees of freedom of the T distribution
     * @return random value from the T(df) distribution
     * @throws NotStrictlyPositiveException if {@code df <= 0}
     */
    public double nextT(double df) throws NotStrictlyPositiveException {
        return new TDistribution(getRan(), df,
                TDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY).sample();
    }

    /**
     * Generates a random value from the {@link WeibullDistribution Weibull Distribution}.
     *
     * @param shape the shape parameter of the Weibull distribution
     * @param scale the scale parameter of the Weibull distribution
     * @return random value sampled from the Weibull(shape, size) distribution
     * @throws NotStrictlyPositiveException if {@code shape <= 0} or
     * {@code scale <= 0}.
     */
    public double nextWeibull(double shape, double scale) throws NotStrictlyPositiveException {
        return new WeibullDistribution(getRan(), shape, scale,
                WeibullDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY).sample();
    }

    /**
     * Generates a random value from the {@link ZipfDistribution Zipf Distribution}.
     *
     * @param numberOfElements the number of elements of the ZipfDistribution
     * @param exponent the exponent of the ZipfDistribution
     * @return random value sampled from the Zipf(numberOfElements, exponent) distribution
     * @exception NotStrictlyPositiveException if {@code numberOfElements <= 0}
     * or {@code exponent <= 0}.
     */
    public int nextZipf(int numberOfElements, double exponent) throws NotStrictlyPositiveException {
        return new ZipfDistribution(getRan(), numberOfElements, exponent).sample();
    }

    /**
     * Generates a random value from the {@link BetaDistribution Beta Distribution}.
     *
     * @param alpha first distribution shape parameter
     * @param beta second distribution shape parameter
     * @return random value sampled from the beta(alpha, beta) distribution
     */
    public double nextBeta(double alpha, double beta) {
        return new BetaDistribution(getRan(), alpha, beta,
                BetaDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY).sample();
    }

    /**
     * Generates a random value from the {@link BinomialDistribution Binomial Distribution}.
     *
     * @param numberOfTrials number of trials of the Binomial distribution
     * @param probabilityOfSuccess probability of success of the Binomial distribution
     * @return random value sampled from the Binomial(numberOfTrials, probabilityOfSuccess) distribution
     */
    public int nextBinomial(int numberOfTrials, double probabilityOfSuccess) {
        return new BinomialDistribution(getRan(), numberOfTrials, probabilityOfSuccess).sample();
    }

    /**
     * Generates a random value from the {@link CauchyDistribution Cauchy Distribution}.
     *
     * @param median the median of the Cauchy distribution
     * @param scale the scale parameter of the Cauchy distribution
     * @return random value sampled from the Cauchy(median, scale) distribution
     */
    public double nextCauchy(double median, double scale) {
        return new CauchyDistribution(getRan(), median, scale,
                CauchyDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY).sample();
    }

    /**
     * Generates a random value from the {@link ChiSquaredDistribution ChiSquare Distribution}.
     *
     * @param df the degrees of freedom of the ChiSquare distribution
     * @return random value sampled from the ChiSquare(df) distribution
     */
    public double nextChiSquare(double df) {
        return new ChiSquaredDistribution(getRan(), df,
                ChiSquaredDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY).sample();
    }

    /**
     * Generates a random value from the {@link FDistribution F Distribution}.
     *
     * @param numeratorDf the numerator degrees of freedom of the F distribution
     * @param denominatorDf the denominator degrees of freedom of the F distribution
     * @return random value sampled from the F(numeratorDf, denominatorDf) distribution
     * @throws NotStrictlyPositiveException if
     * {@code numeratorDf <= 0} or {@code denominatorDf <= 0}.
     */
    public double nextF(double numeratorDf, double denominatorDf) throws NotStrictlyPositiveException {
        return new FDistribution(getRan(), numeratorDf, denominatorDf,
                FDistribution.DEFAULT_INVERSE_ABSOLUTE_ACCURACY).sample();
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * <strong>Algorithm Description</strong>: scales the output of
     * Random.nextDouble(), but rejects 0 values (i.e., will generate another
     * random double if Random.nextDouble() returns 0). This is necessary to
     * provide a symmetric output interval (both endpoints excluded).
     * </p>
     * @throws NumberIsTooLargeException if {@code lower >= upper}
     * @throws NotFiniteNumberException if one of the bounds is infinite
     * @throws NotANumberException if one of the bounds is NaN
     */
    public double nextUniform(double lower, double upper)
        throws NumberIsTooLargeException, NotFiniteNumberException, NotANumberException {
        return nextUniform(lower, upper, false);
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * <strong>Algorithm Description</strong>: if the lower bound is excluded,
     * scales the output of Random.nextDouble(), but rejects 0 values (i.e.,
     * will generate another random double if Random.nextDouble() returns 0).
     * This is necessary to provide a symmetric output interval (both
     * endpoints excluded).
     * </p>
     *
     * @throws NumberIsTooLargeException if {@code lower >= upper}
     * @throws NotFiniteNumberException if one of the bounds is infinite
     * @throws NotANumberException if one of the bounds is NaN
     */
    public double nextUniform(double lower, double upper, boolean lowerInclusive)
        throws NumberIsTooLargeException, NotFiniteNumberException, NotANumberException {

        if (lower >= upper) {
            throw new NumberIsTooLargeException(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND,
                                                lower, upper, false);
        }

        if (Double.isInfinite(lower)) {
            throw new NotFiniteNumberException(LocalizedFormats.INFINITE_BOUND, lower);
        }
        if (Double.isInfinite(upper)) {
            throw new NotFiniteNumberException(LocalizedFormats.INFINITE_BOUND, upper);
        }

        if (Double.isNaN(lower) || Double.isNaN(upper)) {
            throw new NotANumberException();
        }

        final RandomGenerator generator = getRan();

        // ensure nextDouble() isn't 0.0
        double u = generator.nextDouble();
        while (!lowerInclusive && u <= 0.0) {
            u = generator.nextDouble();
        }

        return u * upper + (1.0 - u) * lower;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * Uses a 2-cycle permutation shuffle. The shuffling process is described <a
     * href="http://www.maths.abdn.ac.uk/~igc/tch/mx4002/notes/node83.html">
     * here</a>.
     * </p>
     * @throws NumberIsTooLargeException if {@code k > n}.
     * @throws NotStrictlyPositiveException if {@code k <= 0}.
     */
    public int[] nextPermutation(int n, int k)
        throws NumberIsTooLargeException, NotStrictlyPositiveException {
        if (k > n) {
            throw new NumberIsTooLargeException(LocalizedFormats.PERMUTATION_EXCEEDS_N,
                                                k, n, true);
        }
        if (k <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.PERMUTATION_SIZE,
                                                   k);
        }

        int[] index = getNatural(n);
        shuffle(index, n - k);
        int[] result = new int[k];
        for (int i = 0; i < k; i++) {
            result[i] = index[n - i - 1];
        }

        return result;
    }

    /**
     * {@inheritDoc}
     *
     * <p>
     * <strong>Algorithm Description</strong>: Uses a 2-cycle permutation
     * shuffle to generate a random permutation of <code>c.size()</code> and
     * then returns the elements whose indexes correspond to the elements of the
     * generated permutation. This technique is described, and proven to
     * generate random samples <a
     * href="http://www.maths.abdn.ac.uk/~igc/tch/mx4002/notes/node83.html">
     * here</a>
     * </p>
     */
    public Object[] nextSample(Collection<?> c, int k) throws NumberIsTooLargeException, NotStrictlyPositiveException {

        int len = c.size();
        if (k > len) {
            throw new NumberIsTooLargeException(LocalizedFormats.SAMPLE_SIZE_EXCEEDS_COLLECTION_SIZE,
                                                k, len, true);
        }
        if (k <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_SAMPLES, k);
        }

        Object[] objects = c.toArray();
        int[] index = nextPermutation(len, k);
        Object[] result = new Object[k];
        for (int i = 0; i < k; i++) {
            result[i] = objects[index[i]];
        }
        return result;
    }



    /**
     * Reseeds the random number generator with the supplied seed.
     * <p>
     * Will create and initialize if null.
     * </p>
     *
     * @param seed the seed value to use
     */
    public void reSeed(long seed) {
       getRan().setSeed(seed);
    }

    /**
     * Reseeds the secure random number generator with the current time in
     * milliseconds.
     * <p>
     * Will create and initialize if null.
     * </p>
     */
    public void reSeedSecure() {
        getSecRan().setSeed(System.currentTimeMillis());
    }

    /**
     * Reseeds the secure random number generator with the supplied seed.
     * <p>
     * Will create and initialize if null.
     * </p>
     *
     * @param seed the seed value to use
     */
    public void reSeedSecure(long seed) {
        getSecRan().setSeed(seed);
    }

    /**
     * Reseeds the random number generator with
     * {@code System.currentTimeMillis() + System.identityHashCode(this))}.
     */
    public void reSeed() {
        getRan().setSeed(System.currentTimeMillis() + System.identityHashCode(this));
    }

    /**
     * Sets the PRNG algorithm for the underlying SecureRandom instance using
     * the Security Provider API. The Security Provider API is defined in <a
     * href =
     * "http://java.sun.com/j2se/1.3/docs/guide/security/CryptoSpec.html#AppA">
     * Java Cryptography Architecture API Specification & Reference.</a>
     * <p>
     * <strong>USAGE NOTE:</strong> This method carries <i>significant</i>
     * overhead and may take several seconds to execute.
     * </p>
     *
     * @param algorithm the name of the PRNG algorithm
     * @param provider the name of the provider
     * @throws NoSuchAlgorithmException if the specified algorithm is not available
     * @throws NoSuchProviderException if the specified provider is not installed
     */
    public void setSecureAlgorithm(String algorithm, String provider)
            throws NoSuchAlgorithmException, NoSuchProviderException {
        secRand = SecureRandom.getInstance(algorithm, provider);
    }

    /**
     * Returns the RandomGenerator used to generate non-secure random data.
     * <p>
     * Creates and initializes a default generator if null. Uses a {@link Well19937c}
     * generator with {@code System.currentTimeMillis() + System.identityHashCode(this))}
     * as the default seed.
     * </p>
     *
     * @return the Random used to generate random data
     */
    private RandomGenerator getRan() {
        if (rand == null) {
            initRan();
        }
        return rand;
    }

    /**
     * Sets the default generator to a {@link Well19937c} generator seeded with
     * {@code System.currentTimeMillis() + System.identityHashCode(this))}.
     */
    private void initRan() {
        rand = new Well19937c(System.currentTimeMillis() + System.identityHashCode(this));
    }

    /**
     * Returns the SecureRandom used to generate secure random data.
     * <p>
     * Creates and initializes if null.  Uses
     * {@code System.currentTimeMillis() + System.identityHashCode(this)} as the default seed.
     * </p>
     *
     * @return the SecureRandom used to generate secure random data
     */
    private SecureRandom getSecRan() {
        if (secRand == null) {
            secRand = new SecureRandom();
            secRand.setSeed(System.currentTimeMillis() + System.identityHashCode(this));
        }
        return secRand;
    }

    /**
     * Uses a 2-cycle permutation shuffle to randomly re-order the last elements
     * of list.
     *
     * @param list list to be shuffled
     * @param end element past which shuffling begins
     */
    private void shuffle(int[] list, int end) {
        int target = 0;
        for (int i = list.length - 1; i >= end; i--) {
            if (i == 0) {
                target = 0;
            } else {
                // NumberIsTooLargeException cannot occur
                target = nextInt(0, i);
            }
            int temp = list[target];
            list[target] = list[i];
            list[i] = temp;
        }
    }

    /**
     * Returns an array representing n.
     *
     * @param n the natural number to represent
     * @return array with entries = elements of n
     */
    private int[] getNatural(int n) {
        int[] natural = new int[n];
        for (int i = 0; i < n; i++) {
            natural[i] = i;
        }
        return natural;
    }
}
