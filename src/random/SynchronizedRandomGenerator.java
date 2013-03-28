package random;

public class SynchronizedRandomGenerator implements RandomGenerator {
    /** Object to which all calls will be delegated. */
    private final RandomGenerator wrapped;

    /**
     * Creates a synchronized wrapper for the given {@code RandomGenerator}
     * instance.
     *
     * @param rng Generator whose methods will be called through
     * their corresponding overridden synchronized version.
     * To ensure thread-safety, the wrapped generator <em>must</em>
     * not be used directly.
     */
    public SynchronizedRandomGenerator(RandomGenerator rng) {
        wrapped = rng;
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void setSeed(int seed) {
        wrapped.setSeed(seed);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void setSeed(int[] seed) {
        wrapped.setSeed(seed);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void setSeed(long seed) {
        wrapped.setSeed(seed);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized void nextBytes(byte[] bytes) {
        wrapped.nextBytes(bytes);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized int nextInt() {
        return wrapped.nextInt();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized int nextInt(int n) {
        return wrapped.nextInt(n);
    }

    /**
     * {@inheritDoc}
     */
    public synchronized long nextLong() {
        return wrapped.nextLong();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized boolean nextBoolean() {
        return wrapped.nextBoolean();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized float nextFloat() {
        return wrapped.nextFloat();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized double nextDouble() {
        return wrapped.nextDouble();
    }

    /**
     * {@inheritDoc}
     */
    public synchronized double nextGaussian() {
        return wrapped.nextGaussian();
    }
}
