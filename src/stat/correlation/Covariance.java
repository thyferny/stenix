package stat.correlation;

import linear.BlockRealMatrix;
import linear.RealMatrix;
import stat.descriptive.moment.Mean;
import stat.descriptive.moment.Variance;
import exception.MathIllegalArgumentException;
import exception.util.LocalizedFormats;

public class Covariance {

    /** covariance matrix */
    private final RealMatrix covarianceMatrix;

    /**
     * Create an empty covariance matrix.
     */
    /** Number of observations (length of covariate vectors) */
    private final int n;

    /**
     * Create a Covariance with no data
     */
    public Covariance() {
        super();
        covarianceMatrix = null;
        n = 0;
    }

    /**
     * Create a Covariance matrix from a rectangular array
     * whose columns represent covariates.
     *
     * <p>The <code>biasCorrected</code> parameter determines whether or not
     * covariance estimates are bias-corrected.</p>
     *
     * <p>The input array must be rectangular with at least two columns
     * and two rows.</p>
     *
     * @param data rectangular array with columns representing covariates
     * @param biasCorrected true means covariances are bias-corrected
     * @throws MathIllegalArgumentException if the input data array is not
     * rectangular with at least two rows and two columns.
     */
    public Covariance(double[][] data, boolean biasCorrected)
    throws MathIllegalArgumentException {
        this(new BlockRealMatrix(data), biasCorrected);
    }

    /**
     * Create a Covariance matrix from a rectangular array
     * whose columns represent covariates.
     *
     * <p>The input array must be rectangular with at least two columns
     * and two rows</p>
     *
     * @param data rectangular array with columns representing covariates
     * @throws MathIllegalArgumentException if the input data array is not
     * rectangular with at least two rows and two columns.
     */
    public Covariance(double[][] data) throws MathIllegalArgumentException {
        this(data, true);
    }

    /**
     * Create a covariance matrix from a matrix whose columns
     * represent covariates.
     *
     * <p>The <code>biasCorrected</code> parameter determines whether or not
     * covariance estimates are bias-corrected.</p>
     *
     * <p>The matrix must have at least two columns and two rows</p>
     *
     * @param matrix matrix with columns representing covariates
     * @param biasCorrected true means covariances are bias-corrected
     * @throws MathIllegalArgumentException if the input matrix does not have
     * at least two rows and two columns
     */
    public Covariance(RealMatrix matrix, boolean biasCorrected)
    throws MathIllegalArgumentException {
       checkSufficientData(matrix);
       n = matrix.getRowDimension();
       covarianceMatrix = computeCovarianceMatrix(matrix, biasCorrected);
    }

    /**
     * Create a covariance matrix from a matrix whose columns
     * represent covariates.
     *
     * <p>The matrix must have at least two columns and two rows</p>
     *
     * @param matrix matrix with columns representing covariates
     * @throws MathIllegalArgumentException if the input matrix does not have
     * at least two rows and two columns
     */
    public Covariance(RealMatrix matrix) throws MathIllegalArgumentException {
        this(matrix, true);
    }

    /**
     * Returns the covariance matrix
     *
     * @return covariance matrix
     */
    public RealMatrix getCovarianceMatrix() {
        return covarianceMatrix;
    }

    /**
     * Returns the number of observations (length of covariate vectors)
     *
     * @return number of observations
     */
    public int getN() {
        return n;
    }

    /**
     * Compute a covariance matrix from a matrix whose columns represent
     * covariates.
     * @param matrix input matrix (must have at least two columns and two rows)
     * @param biasCorrected determines whether or not covariance estimates are bias-corrected
     * @return covariance matrix
     * @throws MathIllegalArgumentException if the matrix does not contain sufficient data
     */
    protected RealMatrix computeCovarianceMatrix(RealMatrix matrix, boolean biasCorrected)
    throws MathIllegalArgumentException {
        int dimension = matrix.getColumnDimension();
        Variance variance = new Variance(biasCorrected);
        RealMatrix outMatrix = new BlockRealMatrix(dimension, dimension);
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < i; j++) {
              double cov = covariance(matrix.getColumn(i), matrix.getColumn(j), biasCorrected);
              outMatrix.setEntry(i, j, cov);
              outMatrix.setEntry(j, i, cov);
            }
            outMatrix.setEntry(i, i, variance.evaluate(matrix.getColumn(i)));
        }
        return outMatrix;
    }

    /**
     * Create a covariance matrix from a matrix whose columns represent
     * covariates. Covariances are computed using the bias-corrected formula.
     * @param matrix input matrix (must have at least two columns and two rows)
     * @return covariance matrix
     * @throws MathIllegalArgumentException if matrix does not contain sufficient data
     * @see #Covariance
     */
    protected RealMatrix computeCovarianceMatrix(RealMatrix matrix)
    throws MathIllegalArgumentException {
        return computeCovarianceMatrix(matrix, true);
    }

    /**
     * Compute a covariance matrix from a rectangular array whose columns represent
     * covariates.
     * @param data input array (must have at least two columns and two rows)
     * @param biasCorrected determines whether or not covariance estimates are bias-corrected
     * @return covariance matrix
     * @throws MathIllegalArgumentException if the data array does not contain sufficient
     * data
     */
    protected RealMatrix computeCovarianceMatrix(double[][] data, boolean biasCorrected)
    throws MathIllegalArgumentException {
        return computeCovarianceMatrix(new BlockRealMatrix(data), biasCorrected);
    }

    /**
     * Create a covariance matrix from a rectangular array whose columns represent
     * covariates. Covariances are computed using the bias-corrected formula.
     * @param data input array (must have at least two columns and two rows)
     * @return covariance matrix
     * @throws MathIllegalArgumentException if the data array does not contain sufficient data
     * @see #Covariance
     */
    protected RealMatrix computeCovarianceMatrix(double[][] data) throws MathIllegalArgumentException {
        return computeCovarianceMatrix(data, true);
    }

    /**
     * Computes the covariance between the two arrays.
     *
     * <p>Array lengths must match and the common length must be at least 2.</p>
     *
     * @param xArray first data array
     * @param yArray second data array
     * @param biasCorrected if true, returned value will be bias-corrected
     * @return returns the covariance for the two arrays
     * @throws  MathIllegalArgumentException if the arrays lengths do not match or
     * there is insufficient data
     */
    public double covariance(final double[] xArray, final double[] yArray, boolean biasCorrected)
        throws MathIllegalArgumentException {
        Mean mean = new Mean();
        double result = 0d;
        int length = xArray.length;
        if (length != yArray.length) {
            throw new MathIllegalArgumentException(
                  LocalizedFormats.DIMENSIONS_MISMATCH_SIMPLE, length, yArray.length);
        } else if (length < 2) {
            throw new MathIllegalArgumentException(
                  LocalizedFormats.INSUFFICIENT_OBSERVED_POINTS_IN_SAMPLE, length, 2);
        } else {
            double xMean = mean.evaluate(xArray);
            double yMean = mean.evaluate(yArray);
            for (int i = 0; i < length; i++) {
                double xDev = xArray[i] - xMean;
                double yDev = yArray[i] - yMean;
                result += (xDev * yDev - result) / (i + 1);
            }
        }
        return biasCorrected ? result * ((double) length / (double)(length - 1)) : result;
    }

    /**
     * Computes the covariance between the two arrays, using the bias-corrected
     * formula.
     *
     * <p>Array lengths must match and the common length must be at least 2.</p>
     *
     * @param xArray first data array
     * @param yArray second data array
     * @return returns the covariance for the two arrays
     * @throws  MathIllegalArgumentException if the arrays lengths do not match or
     * there is insufficient data
     */
    public double covariance(final double[] xArray, final double[] yArray)
        throws MathIllegalArgumentException {
        return covariance(xArray, yArray, true);
    }

    /**
     * Throws MathIllegalArgumentException if the matrix does not have at least
     * two columns and two rows.
     * @param matrix matrix to check
     * @throws MathIllegalArgumentException if the matrix does not contain sufficient data
     * to compute covariance
     */
    private void checkSufficientData(final RealMatrix matrix) throws MathIllegalArgumentException {
        int nRows = matrix.getRowDimension();
        int nCols = matrix.getColumnDimension();
        if (nRows < 2 || nCols < 2) {
            throw new MathIllegalArgumentException(
                    LocalizedFormats.INSUFFICIENT_ROWS_AND_COLUMNS,
                    nRows, nCols);
        }
    }
}
