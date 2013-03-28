package analysis;

public interface UnivariateMatrixFunction {

    /**
     * Compute the value for the function.
     * @param x the point for which the function value should be computed
     * @return the value
     */
    double[][] value(double x);

}
