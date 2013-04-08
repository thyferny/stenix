package ode;

import exception.DimensionMismatchException;
import exception.MaxCountExceededException;
import exception.NoBracketingException;
import exception.NumberIsTooSmallException;


public interface FirstOrderIntegrator extends ODEIntegrator {

  /** Integrate the differential equations up to the given time.
   * <p>This method solves an Initial Value Problem (IVP).</p>
   * <p>Since this method stores some internal state variables made
   * available in its public interface during integration ({@link
   * #getCurrentSignedStepsize()}), it is <em>not</em> thread-safe.</p>
   * @param equations differential equations to integrate
   * @param t0 initial time
   * @param y0 initial value of the state vector at t0
   * @param t target time for the integration
   * (can be set to a value smaller than <code>t0</code> for backward integration)
   * @param y placeholder where to put the state vector at each successful
   *  step (and hence at the end of integration), can be the same object as y0
   * @return stop time, will be the same as target time if integration reached its
   * target, but may be different if some {@link
   * org.apache.commons.math3.ode.events.EventHandler} stops it at some point.
   * @exception DimensionMismatchException if arrays dimension do not match equations settings
   * @exception NumberIsTooSmallException if integration step is too small
   * @exception MaxCountExceededException if the number of functions evaluations is exceeded
   * @exception NoBracketingException if the location of an event cannot be bracketed
   */
  double integrate (FirstOrderDifferentialEquations equations,
                    double t0, double[] y0, double t, double[] y)
      throws DimensionMismatchException, NumberIsTooSmallException,
             MaxCountExceededException, NoBracketingException;

}
