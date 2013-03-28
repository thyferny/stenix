package random;

import math.util.FastMath;

public class UnitSphereRandomVectorGenerator
    implements RandomVectorGenerator {
    /**
     * RNG used for generating the individual components of the vectors.
     */
    private final RandomGenerator rand;
    /**
     * Space dimension.
     */
    private final int dimension;

    /**
     * @param dimension Space dimension.
     * @param rand RNG for the individual components of the vectors.
     */
    public UnitSphereRandomVectorGenerator(final int dimension,
                                           final RandomGenerator rand) {
        this.dimension = dimension;
        this.rand = rand;
    }
    /**
     * Create an object that will use a default RNG ({@link MersenneTwister}),
     * in order to generate the individual components.
     *
     * @param dimension Space dimension.
     */
    public UnitSphereRandomVectorGenerator(final int dimension) {
        this(dimension, new MersenneTwister());
    }

    /** {@inheritDoc} */
    public double[] nextVector() {

        final double[] v = new double[dimension];

        double normSq;
        do {
            normSq = 0;
            for (int i = 0; i < dimension; i++) {
                final double comp = 2 * rand.nextDouble() - 1;
                v[i] = comp;
                normSq += comp * comp;
            }
        } while (normSq > 1);

        final double f = 1 / FastMath.sqrt(normSq);
        for (int i = 0; i < dimension; i++) {
            v[i] *= f;
        }

        return v;

    }

}
