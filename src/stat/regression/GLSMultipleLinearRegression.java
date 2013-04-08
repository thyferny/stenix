package stat.regression;

import linear.Array2DRowRealMatrix;
import linear.LUDecomposition;
import linear.RealMatrix;
import linear.RealVector;

public class GLSMultipleLinearRegression extends AbstractMultipleLinearRegression {

    /** Covariance matrix. */
    private RealMatrix Omega;

    /** Inverse of covariance matrix. */
    private RealMatrix OmegaInverse;

    /** Replace sample data, overriding any previous sample.
     * @param y y values of the sample
     * @param x x values of the sample
     * @param covariance array representing the covariance matrix
     */
    public void newSampleData(double[] y, double[][] x, double[][] covariance) {
        validateSampleData(x, y);
        newYSampleData(y);
        newXSampleData(x);
        validateCovarianceData(x, covariance);
        newCovarianceData(covariance);
    }

    /**
     * Add the covariance data.
     *
     * @param omega the [n,n] array representing the covariance
     */
    protected void newCovarianceData(double[][] omega){
        this.Omega = new Array2DRowRealMatrix(omega);
        this.OmegaInverse = null;
    }

    /**
     * Get the inverse of the covariance.
     * <p>The inverse of the covariance matrix is lazily evaluated and cached.</p>
     * @return inverse of the covariance
     */
    protected RealMatrix getOmegaInverse() {
        if (OmegaInverse == null) {
            OmegaInverse = new LUDecomposition(Omega).getSolver().getInverse();
        }
        return OmegaInverse;
    }

    /**
     * Calculates beta by GLS.
     * <pre>
     *  b=(X' Omega^-1 X)^-1X'Omega^-1 y
     * </pre>
     * @return beta
     */
    @Override
    protected RealVector calculateBeta() {
        RealMatrix OI = getOmegaInverse();
        RealMatrix XT = getX().transpose();
        RealMatrix XTOIX = XT.multiply(OI).multiply(getX());
        RealMatrix inverse = new LUDecomposition(XTOIX).getSolver().getInverse();
        return inverse.multiply(XT).multiply(OI).operate(getY());
    }

    /**
     * Calculates the variance on the beta.
     * <pre>
     *  Var(b)=(X' Omega^-1 X)^-1
     * </pre>
     * @return The beta variance matrix
     */
    @Override
    protected RealMatrix calculateBetaVariance() {
        RealMatrix OI = getOmegaInverse();
        RealMatrix XTOIX = getX().transpose().multiply(OI).multiply(getX());
        return new LUDecomposition(XTOIX).getSolver().getInverse();
    }


    /**
     * Calculates the estimated variance of the error term using the formula
     * <pre>
     *  Var(u) = Tr(u' Omega^-1 u)/(n-k)
     * </pre>
     * where n and k are the row and column dimensions of the design
     * matrix X.
     *
     * @return error variance
     * @since 2.2
     */
    @Override
    protected double calculateErrorVariance() {
        RealVector residuals = calculateResiduals();
        double t = residuals.dotProduct(getOmegaInverse().operate(residuals));
        return t / (getX().getRowDimension() - getX().getColumnDimension());

    }

}
