package random;

import java.util.Random;

public class JDKRandomGenerator extends Random implements RandomGenerator {

    /** Serializable version identifier. */
    private static final long serialVersionUID = -7745277476784028798L;

    /** {@inheritDoc} */
    public void setSeed(int seed) {
        setSeed((long) seed);
    }

    /** {@inheritDoc} */
    public void setSeed(int[] seed) {
        // the following number is the largest prime that fits in 32 bits (it is 2^32 - 5)
        final long prime = 4294967291l;

        long combined = 0l;
        for (int s : seed) {
            combined = combined * prime + s;
        }
        setSeed(combined);
    }

}
