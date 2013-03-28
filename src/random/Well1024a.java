package random;

public class Well1024a extends AbstractWell {

    /** Serializable version identifier. */
    private static final long serialVersionUID = 5680173464174485492L;

    /** Number of bits in the pool. */
    private static final int K = 1024;

    /** First parameter of the algorithm. */
    private static final int M1 = 3;

    /** Second parameter of the algorithm. */
    private static final int M2 = 24;

    /** Third parameter of the algorithm. */
    private static final int M3 = 10;

    /** Creates a new random number generator.
     * <p>The instance is initialized using the current time as the
     * seed.</p>
     */
    public Well1024a() {
        super(K, M1, M2, M3);
    }

    /** Creates a new random number generator using a single int seed.
     * @param seed the initial seed (32 bits integer)
     */
    public Well1024a(int seed) {
        super(K, M1, M2, M3, seed);
    }

    /** Creates a new random number generator using an int array seed.
     * @param seed the initial seed (32 bits integers array), if null
     * the seed of the generator will be related to the current time
     */
    public Well1024a(int[] seed) {
        super(K, M1, M2, M3, seed);
    }

    /** Creates a new random number generator using a single long seed.
     * @param seed the initial seed (64 bits integer)
     */
    public Well1024a(long seed) {
        super(K, M1, M2, M3, seed);
    }

    /** {@inheritDoc} */
    @Override
    protected int next(final int bits) {

        final int indexRm1 = iRm1[index];

        final int v0       = v[index];
        final int vM1      = v[i1[index]];
        final int vM2      = v[i2[index]];
        final int vM3      = v[i3[index]];

        final int z0 = v[indexRm1];
        final int z1 = v0  ^ (vM1 ^ (vM1 >>> 8));
        final int z2 = (vM2 ^ (vM2 << 19)) ^ (vM3 ^ (vM3 << 14));
        final int z3 = z1      ^ z2;
        final int z4 = (z0 ^ (z0 << 11)) ^ (z1 ^ (z1 << 7)) ^ (z2 ^ (z2 << 13));

        v[index]     = z3;
        v[indexRm1]  = z4;
        index        = indexRm1;

        return z4 >>> (32 - bits);

    }
}
