package ode;

import java.util.ArrayList;
import java.util.Collection;

import exception.DimensionMismatchException;
import exception.MaxCountExceededException;

class ParameterizedWrapper implements ParameterizedODE {

    /** Basic FODE without parameter. */
    private final FirstOrderDifferentialEquations fode;

    /** Simple constructor.
     * @param ode original first order differential equations
     */
    public ParameterizedWrapper(final FirstOrderDifferentialEquations ode) {
        this.fode = ode;
    }

    /** Get the dimension of the underlying FODE.
     * @return dimension of the underlying FODE
     */
    public int getDimension() {
        return fode.getDimension();
    }

    /** Get the current time derivative of the state vector of the underlying FODE.
     * @param t current value of the independent <I>time</I> variable
     * @param y array containing the current value of the state vector
     * @param yDot placeholder array where to put the time derivative of the state vector
     * @exception MaxCountExceededException if the number of functions evaluations is exceeded
     * @exception DimensionMismatchException if arrays dimensions do not match equations settings
     */
    public void computeDerivatives(double t, double[] y, double[] yDot)
        throws MaxCountExceededException, DimensionMismatchException {
        fode.computeDerivatives(t, y, yDot);
    }

    /** {@inheritDoc} */
    public Collection<String> getParametersNames() {
        return new ArrayList<String>();
    }

    /** {@inheritDoc} */
    public boolean isSupported(String name) {
        return false;
    }

    /** {@inheritDoc} */
    public double getParameter(String name)
        throws UnknownParameterException {
        if (!isSupported(name)) {
            throw new UnknownParameterException(name);
        }
        return Double.NaN;
    }

    /** {@inheritDoc} */
    public void setParameter(String name, double value) {
    }

}
