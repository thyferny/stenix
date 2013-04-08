package ode;

import exception.DimensionMismatchException;
import exception.MaxCountExceededException;

public interface MainStateJacobianProvider extends FirstOrderDifferentialEquations {

    /** Compute the jacobian matrix of ODE with respect to main state.
     * @param t current value of the independent <I>time</I> variable
     * @param y array containing the current value of the main state vector
     * @param yDot array containing the current value of the time derivative of the main state vector
     * @param dFdY placeholder array where to put the jacobian matrix of the ODE w.r.t. the main state vector
     * @exception MaxCountExceededException if the number of functions evaluations is exceeded
     * @exception DimensionMismatchException if arrays dimensions do not match equations settings
     */
    void computeMainStateJacobian(double t, double[] y, double[] yDot, double[][] dFdY)
        throws MaxCountExceededException, DimensionMismatchException;

}
