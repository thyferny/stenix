package analysis;

public interface UnivariateFunction {
    /**
     * Compute the value of the function.
     *
     * @param x Point at which the function value should be computed.
     * @return the value of the function.
     * @throws IllegalArgumentException when the activated method itself can
     * ascertain that a precondition, specified in the API expressed at the
     * level of the activated method, has been violated.
     * When Commons Math throws an {@code IllegalArgumentException}, it is
     * usually the consequence of checking the actual parameters passed to
     * the method.
     */
    double value(double x);
}
