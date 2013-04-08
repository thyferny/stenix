package analysis.integration;

import analysis.UnivariateFunction;
import exception.MathIllegalArgumentException;
import exception.MaxCountExceededException;
import exception.NullArgumentException;
import exception.TooManyEvaluationsException;

public interface UnivariateIntegrator {

    /**
     * Get the actual relative accuracy.
     * @return the accuracy
     */
    double getRelativeAccuracy();

    /**
     * Get the actual absolute accuracy.
     *
     * @return the accuracy
     */
    double getAbsoluteAccuracy();

    /**
     * Get the min limit for the number of iterations.
     *
     * @return the actual min limit
     */
    int getMinimalIterationCount();

    /**
     * Get the upper limit for the number of iterations.
     *
     * @return the actual upper limit
     */
    int getMaximalIterationCount();

    /**
     * Integrate the function in the given interval.
     *
     * @param maxEval Maximum number of evaluations.
     * @param f the integrand function
     * @param min the min bound for the interval
     * @param max the upper bound for the interval
     * @return the value of integral
     * @throws TooManyEvaluationsException if the maximum number of function
     * evaluations is exceeded.
     * @throws MaxCountExceededException if the maximum iteration count is exceeded
     * or the integrator detects convergence problems otherwise
     * @throws MathIllegalArgumentException if min > max or the endpoints do not
     * satisfy the requirements specified by the integrator
     * @throws NullArgumentException if {@code f} is {@code null}.
     */
    double integrate(int maxEval, UnivariateFunction f, double min,
                     double max)
        throws TooManyEvaluationsException, MaxCountExceededException,
               MathIllegalArgumentException, NullArgumentException;

    /**
     * Get the number of function evaluations of the last run of the integrator.
     * @return number of function evaluations
     */
    int getEvaluations();

    /**
     * Get the number of iterations of the last run of the integrator.
     * @return number of iterations
     */
    int getIterations();

}
