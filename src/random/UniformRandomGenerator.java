package random;

import math.util.FastMath;

public class UniformRandomGenerator implements NormalizedRandomGenerator {

    /** Square root of three. */
    private static final double SQRT3 = FastMath.sqrt(3.0);

    /** Underlying generator. */
    private final RandomGenerator generator;

    /** Create a new generator.
     * @param generator underlying random generator to use
     */
    public UniformRandomGenerator(RandomGenerator generator) {
        this.generator = generator;
    }

    /** Generate a random scalar with null mean and unit standard deviation.
     * <p>The number generated is uniformly distributed between -&sqrt;(3)
     * and +&sqrt;(3).</p>
     * @return a random scalar with null mean and unit standard deviation
     */
    public double nextNormalizedDouble() {
        return SQRT3 * (2 * generator.nextDouble() - 1.0);
    }

}
