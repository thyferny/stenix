//package random;
//
//import exception.DimensionMismatchException;
//
//public class CorrelatedRandomVectorGenerator
//    implements RandomVectorGenerator {
//    /** Mean vector. */
//    private final double[] mean;
//    /** Underlying generator. */
//    private final NormalizedRandomGenerator generator;
//    /** Storage for the normalized vector. */
//    private final double[] normalized;
//    /** Root of the covariance matrix. */
//    private final RealMatrix root;
//
//    /**
//     * Builds a correlated random vector generator from its mean
//     * vector and covariance matrix.
//     *
//     * @param mean Expected mean values for all components.
//     * @param covariance Covariance matrix.
//     * @param small Diagonal elements threshold under which  column are
//     * considered to be dependent on previous ones and are discarded
//     * @param generator underlying generator for uncorrelated normalized
//     * components.
//     * @throws org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException
//     * if the covariance matrix is not strictly positive definite.
//     * @throws DimensionMismatchException if the mean and covariance
//     * arrays dimensions do not match.
//     */
//    public CorrelatedRandomVectorGenerator(double[] mean,
//                                           RealMatrix covariance, double small,
//                                           NormalizedRandomGenerator generator) {
//        int order = covariance.getRowDimension();
//        if (mean.length != order) {
//            throw new DimensionMismatchException(mean.length, order);
//        }
//        this.mean = mean.clone();
//
//        final RectangularCholeskyDecomposition decomposition =
//            new RectangularCholeskyDecomposition(covariance, small);
//        root = decomposition.getRootMatrix();
//
//        this.generator = generator;
//        normalized = new double[decomposition.getRank()];
//
//    }
//
//    /**
//     * Builds a null mean random correlated vector generator from its
//     * covariance matrix.
//     *
//     * @param covariance Covariance matrix.
//     * @param small Diagonal elements threshold under which  column are
//     * considered to be dependent on previous ones and are discarded.
//     * @param generator Underlying generator for uncorrelated normalized
//     * components.
//     * @throws org.apache.commons.math3.linear.NonPositiveDefiniteMatrixException
//     * if the covariance matrix is not strictly positive definite.
//     */
//    public CorrelatedRandomVectorGenerator(RealMatrix covariance, double small,
//                                           NormalizedRandomGenerator generator) {
//        int order = covariance.getRowDimension();
//        mean = new double[order];
//        for (int i = 0; i < order; ++i) {
//            mean[i] = 0;
//        }
//
//        final RectangularCholeskyDecomposition decomposition =
//            new RectangularCholeskyDecomposition(covariance, small);
//        root = decomposition.getRootMatrix();
//
//        this.generator = generator;
//        normalized = new double[decomposition.getRank()];
//
//    }
//
//    /** Get the underlying normalized components generator.
//     * @return underlying uncorrelated components generator
//     */
//    public NormalizedRandomGenerator getGenerator() {
//        return generator;
//    }
//
//    /** Get the rank of the covariance matrix.
//     * The rank is the number of independent rows in the covariance
//     * matrix, it is also the number of columns of the root matrix.
//     * @return rank of the square matrix.
//     * @see #getRootMatrix()
//     */
//    public int getRank() {
//        return normalized.length;
//    }
//
//    /** Get the root of the covariance matrix.
//     * The root is the rectangular matrix <code>B</code> such that
//     * the covariance matrix is equal to <code>B.B<sup>T</sup></code>
//     * @return root of the square matrix
//     * @see #getRank()
//     */
//    public RealMatrix getRootMatrix() {
//        return root;
//    }
//
//    /** Generate a correlated random vector.
//     * @return a random vector as an array of double. The returned array
//     * is created at each call, the caller can do what it wants with it.
//     */
//    public double[] nextVector() {
//
//        // generate uncorrelated vector
//        for (int i = 0; i < normalized.length; ++i) {
//            normalized[i] = generator.nextNormalizedDouble();
//        }
//
//        // compute correlated vector
//        double[] correlated = new double[mean.length];
//        for (int i = 0; i < correlated.length; ++i) {
//            correlated[i] = mean[i];
//            for (int j = 0; j < root.getColumnDimension(); ++j) {
//                correlated[i] += root.getEntry(i, j) * normalized[j];
//            }
//        }
//
//        return correlated;
//
//    }
//
//}
