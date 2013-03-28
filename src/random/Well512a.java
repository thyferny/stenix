package random;

public class Well512a extends AbstractWell {

    /** Serializable version identifier. */
    private static final long serialVersionUID = -6104179812103820574L;

    /** Number of bits in the pool. */
    private static final int K = 512;

    /** First parameter of the algorithm. */
    private static final int M1 = 13;

    /** Second parameter of the algorithm. */
    private static final int M2 = 9;

    /** Third parameter of the algorithm. */
    private static final int M3 = 5;

    /** Creates a new random number generator.
     * <p>The instance is initialized using the current time as the
     * seed.</p>
     */
    public Well512a() {
        super(K, M1, M2, M3);
    }

    /** Creates a new random number generator using a single int seed.
     * @param seed the initial seed (32 bits integer)
     */
    public Well512a(int seed) {
        super(K, M1, M2, M3, seed);
    }

    /** Creates a new random number generator using an int array seed.
     * @param seed the initial seed (32 bits integers array), if null
     * the seed of the generator will be related to the current time
     */
    public Well512a(int[] seed) {
        super(K, M1, M2, M3, seed);
    }

    /** Creates a new random number generator using a single long seed.
     * @param seed the initial seed (64 bits integer)
     */
    public Well512a(long seed) {
        super(K, M1, M2, M3, seed);
    }

    /** {@inheritDoc} */
    @Override
    protected int next(final int bits) {

        final int indexRm1 = iRm1[index];

        final int vi = v[index];
        final int vi1 = v[i1[index]];
        final int vi2 = v[i2[index]];
        final int z0 = v[indexRm1];

        // the values below include the errata of the original article
        final int z1 = (vi ^ (vi << 16))   ^ (vi1 ^ (vi1 << 15));
        final int z2 = vi2 ^ (vi2 >>> 11);
        final int z3 = z1 ^ z2;
        final int z4 = (z0 ^ (z0 << 2)) ^ (z1 ^ (z1 << 18)) ^ (z2 << 28) ^ (z3 ^ ((z3 << 5) & 0xda442d24));

        v[index] = z3;
        v[indexRm1]  = z4;
        index    = indexRm1;

        return z4 >>> (32 - bits);

    }

}
