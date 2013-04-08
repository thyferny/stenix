package ode;

import exception.DimensionMismatchException;
import exception.MaxCountExceededException;


public interface SecondaryEquations {

    /** Get the dimension of the secondary state parameters.
     * @return dimension of the secondary state parameters
     */
    int getDimension();

    /** Compute the derivatives related to the secondary state parameters.
     * @param t current value of the independent <I>time</I> variable
     * @param primary array containing the current value of the primary state vector
     * @param primaryDot array containing the derivative of the primary state vector
     * @param secondary array containing the current value of the secondary state vector
     * @param secondaryDot placeholder array where to put the derivative of the secondary state vector
     * @exception MaxCountExceededException if the number of functions evaluations is exceeded
     * @exception DimensionMismatchException if arrays dimensions do not match equations settings
     */
    void computeDerivatives(double t, double[] primary, double[] primaryDot,
                            double[] secondary, double[] secondaryDot)
        throws MaxCountExceededException, DimensionMismatchException;

}
