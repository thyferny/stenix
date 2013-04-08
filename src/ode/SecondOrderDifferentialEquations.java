package ode;

public interface SecondOrderDifferentialEquations {

    /** Get the dimension of the problem.
     * @return dimension of the problem
     */
    int getDimension();

    /** Get the current time derivative of the state vector.
     * @param t current value of the independent <I>time</I> variable
     * @param y array containing the current value of the state vector
     * @param yDot array containing the current value of the first derivative
     * of the state vector
     * @param yDDot placeholder array where to put the second time derivative
     * of the state vector
     */
    void computeSecondDerivatives(double t, double[] y, double[] yDot, double[] yDDot);

}
