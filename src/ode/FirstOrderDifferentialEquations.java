package ode;

import exception.DimensionMismatchException;
import exception.MaxCountExceededException;

public interface FirstOrderDifferentialEquations {

    /** Get the dimension of the problem.
     * @return dimension of the problem
     */
    int getDimension();

    /** Get the current time derivative of the state vector.
     * @param t current value of the independent <I>time</I> variable
     * @param y array containing the current value of the state vector
     * @param yDot placeholder array where to put the time derivative of the state vector
     * @exception MaxCountExceededException if the number of functions evaluations is exceeded
     * @exception DimensionMismatchException if arrays dimensions do not match equations settings
     */
    void computeDerivatives(double t, double[] y, double[] yDot)
        throws MaxCountExceededException, DimensionMismatchException;

}
