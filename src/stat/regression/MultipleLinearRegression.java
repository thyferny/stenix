package stat.regression;

public interface MultipleLinearRegression {

    /**
     * Estimates the regression parameters b.
     *
     * @return The [k,1] array representing b
     */
    double[] estimateRegressionParameters();

    /**
     * Estimates the variance of the regression parameters, ie Var(b).
     *
     * @return The [k,k] array representing the variance of b
     */
    double[][] estimateRegressionParametersVariance();

    /**
     * Estimates the residuals, ie u = y - X*b.
     *
     * @return The [n,1] array representing the residuals
     */
    double[] estimateResiduals();

    /**
     * Returns the variance of the regressand, ie Var(y).
     *
     * @return The double representing the variance of y
     */
    double estimateRegressandVariance();

    /**
     * Returns the standard errors of the regression parameters.
     *
     * @return standard errors of estimated regression parameters
     */
     double[] estimateRegressionParametersStandardErrors();

}
