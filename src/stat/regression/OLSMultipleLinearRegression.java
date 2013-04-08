package stat.regression;

import linear.Array2DRowRealMatrix;
import linear.LUDecomposition;
import linear.QRDecomposition;
import linear.RealMatrix;
import linear.RealVector;
import stat.StatUtils;
import stat.descriptive.moment.SecondMoment;
import exception.MathIllegalArgumentException;

public class OLSMultipleLinearRegression extends AbstractMultipleLinearRegression {

    /** Cached QR decomposition of X matrix */
    private QRDecomposition qr = null;

    /**
     * Loads model x and y sample data, overriding any previous sample.
     *
     * Computes and caches QR decomposition of the X matrix.
     * @param y the [n,1] array representing the y sample
     * @param x the [n,k] array representing the x sample
     * @throws MathIllegalArgumentException if the x and y array data are not
     *             compatible for the regression
     */
    public void newSampleData(double[] y, double[][] x) throws MathIllegalArgumentException {
        validateSampleData(x, y);
        newYSampleData(y);
        newXSampleData(x);
    }

    /**
     * {@inheritDoc}
     * <p>This implementation computes and caches the QR decomposition of the X matrix.</p>
     */
    @Override
    public void newSampleData(double[] data, int nobs, int nvars) {
        super.newSampleData(data, nobs, nvars);
        qr = new QRDecomposition(getX());
    }

    /**
     * <p>Compute the "hat" matrix.
     * </p>
     * <p>The hat matrix is defined in terms of the design matrix X
     *  by X(X<sup>T</sup>X)<sup>-1</sup>X<sup>T</sup>
     * </p>
     * <p>The implementation here uses the QR decomposition to compute the
     * hat matrix as Q I<sub>p</sub>Q<sup>T</sup> where I<sub>p</sub> is the
     * p-dimensional identity matrix augmented by 0's.  This computational
     * formula is from "The Hat Matrix in Regression and ANOVA",
     * David C. Hoaglin and Roy E. Welsch,
     * <i>The American Statistician</i>, Vol. 32, No. 1 (Feb., 1978), pp. 17-22.
     * </p>
     * <p>Data for the model must have been successfully loaded using one of
     * the {@code newSampleData} methods before invoking this method; otherwise
     * a {@code NullPointerException} will be thrown.</p>
     *
     * @return the hat matrix
     */
    public RealMatrix calculateHat() {
        // Create augmented identity matrix
        RealMatrix Q = qr.getQ();
        final int p = qr.getR().getColumnDimension();
        final int n = Q.getColumnDimension();
        // No try-catch or advertised NotStrictlyPositiveException - NPE above if n < 3
        Array2DRowRealMatrix augI = new Array2DRowRealMatrix(n, n);
        double[][] augIData = augI.getDataRef();
        for (int i = 0; i < n; i++) {
            for (int j =0; j < n; j++) {
                if (i == j && i < p) {
                    augIData[i][j] = 1d;
                } else {
                    augIData[i][j] = 0d;
                }
            }
        }

        // Compute and return Hat matrix
        // No DME advertised - args valid if we get here
        return Q.multiply(augI).multiply(Q.transpose());
    }

    /**
     * <p>Returns the sum of squared deviations of Y from its mean.</p>
     *
     * <p>If the model has no intercept term, <code>0</code> is used for the
     * mean of Y - i.e., what is returned is the sum of the squared Y values.</p>
     *
     * <p>The value returned by this method is the SSTO value used in
     * the {@link #calculateRSquared() R-squared} computation.</p>
     *
     * @return SSTO - the total sum of squares
     * @throws MathIllegalArgumentException if the sample has not been set or does
     * not contain at least 3 observations
     * @see #isNoIntercept()
     * @since 2.2
     */
    public double calculateTotalSumOfSquares() throws MathIllegalArgumentException {
        if (isNoIntercept()) {
            return StatUtils.sumSq(getY().toArray());
        } else {
            return new SecondMoment().evaluate(getY().toArray());
        }
    }

    /**
     * Returns the sum of squared residuals.
     *
     * @return residual sum of squares
     * @since 2.2
     */
    public double calculateResidualSumOfSquares() {
        final RealVector residuals = calculateResiduals();
        // No advertised DME, args are valid
        return residuals.dotProduct(residuals);
    }

    /**
     * Returns the R-Squared statistic, defined by the formula <pre>
     * R<sup>2</sup> = 1 - SSR / SSTO
     * </pre>
     * where SSR is the {@link #calculateResidualSumOfSquares() sum of squared residuals}
     * and SSTO is the {@link #calculateTotalSumOfSquares() total sum of squares}
     *
     * @return R-square statistic
     * @throws MathIllegalArgumentException if the sample has not been set or does
     * not contain at least 3 observations
     * @since 2.2
     */
    public double calculateRSquared() throws MathIllegalArgumentException {
        return 1 - calculateResidualSumOfSquares() / calculateTotalSumOfSquares();
    }

    /**
     * <p>Returns the adjusted R-squared statistic, defined by the formula <pre>
     * R<sup>2</sup><sub>adj</sub> = 1 - [SSR (n - 1)] / [SSTO (n - p)]
     * </pre>
     * where SSR is the {@link #calculateResidualSumOfSquares() sum of squared residuals},
     * SSTO is the {@link #calculateTotalSumOfSquares() total sum of squares}, n is the number
     * of observations and p is the number of parameters estimated (including the intercept).</p>
     *
     * <p>If the regression is estimated without an intercept term, what is returned is <pre>
     * <code> 1 - (1 - {@link #calculateRSquared()}) * (n / (n - p)) </code>
     * </pre></p>
     *
     * @return adjusted R-Squared statistic
     * @throws MathIllegalArgumentException if the sample has not been set or does
     * not contain at least 3 observations
     * @see #isNoIntercept()
     * @since 2.2
     */
    public double calculateAdjustedRSquared() throws MathIllegalArgumentException {
        final double n = getX().getRowDimension();
        if (isNoIntercept()) {
            return 1 - (1 - calculateRSquared()) * (n / (n - getX().getColumnDimension()));
        } else {
            return 1 - (calculateResidualSumOfSquares() * (n - 1)) /
                (calculateTotalSumOfSquares() * (n - getX().getColumnDimension()));
        }
    }

    /**
     * {@inheritDoc}
     * <p>This implementation computes and caches the QR decomposition of the X matrix
     * once it is successfully loaded.</p>
     */
    @Override
    protected void newXSampleData(double[][] x) {
        super.newXSampleData(x);
        qr = new QRDecomposition(getX());
    }

    /**
     * Calculates the regression coefficients using OLS.
     *
     * <p>Data for the model must have been successfully loaded using one of
     * the {@code newSampleData} methods before invoking this method; otherwise
     * a {@code NullPointerException} will be thrown.</p>
     *
     * @return beta
     */
    @Override
    protected RealVector calculateBeta() {
        return qr.getSolver().solve(getY());
    }

    /**
     * <p>Calculates the variance-covariance matrix of the regression parameters.
     * </p>
     * <p>Var(b) = (X<sup>T</sup>X)<sup>-1</sup>
     * </p>
     * <p>Uses QR decomposition to reduce (X<sup>T</sup>X)<sup>-1</sup>
     * to (R<sup>T</sup>R)<sup>-1</sup>, with only the top p rows of
     * R included, where p = the length of the beta vector.</p>
     *
     * <p>Data for the model must have been successfully loaded using one of
     * the {@code newSampleData} methods before invoking this method; otherwise
     * a {@code NullPointerException} will be thrown.</p>
     *
     * @return The beta variance-covariance matrix
     */
    @Override
    protected RealMatrix calculateBetaVariance() {
        int p = getX().getColumnDimension();
        RealMatrix Raug = qr.getR().getSubMatrix(0, p - 1 , 0, p - 1);
        RealMatrix Rinv = new LUDecomposition(Raug).getSolver().getInverse();
        return Rinv.multiply(Rinv.transpose());
    }

}
