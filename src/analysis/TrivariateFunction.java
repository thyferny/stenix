package analysis;

public interface TrivariateFunction {
    /**
     * Compute the value for the function.
     *
     * @param x x-coordinate for which the function value should be computed.
     * @param y y-coordinate for which the function value should be computed.
     * @param z z-coordinate for which the function value should be computed.
     * @return the value.
     */
    double value(double x, double y, double z);
}
