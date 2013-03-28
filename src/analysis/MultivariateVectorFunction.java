package analysis;

public interface MultivariateVectorFunction {

    /**
     * Compute the value for the function at the given point.
     * @param point point at which the function must be evaluated
     * @return function value for the given point
     * @exception IllegalArgumentException if points dimension is wrong
     */
    double[] value(double[] point)
        throws IllegalArgumentException;

}
