package ode.sampling;

import java.io.Externalizable;

import exception.MaxCountExceededException;

public interface StepInterpolator extends Externalizable {

  /**
   * Get the previous grid point time.
   * @return previous grid point time
   */
  double getPreviousTime();

  /**
   * Get the current grid point time.
   * @return current grid point time
   */
  double getCurrentTime();

  /**
   * Get the time of the interpolated point.
   * If {@link #setInterpolatedTime} has not been called, it returns
   * the current grid point time.
   * @return interpolation point time
   */
  double getInterpolatedTime();

  /**
   * Set the time of the interpolated point.
   * <p>Setting the time outside of the current step is now allowed, but
   * should be used with care since the accuracy of the interpolator will
   * probably be very poor far from this step. This allowance has been
   * added to simplify implementation of search algorithms near the
   * step endpoints.</p>
   * <p>Setting the time changes the instance internal state. If a
   * specific state must be preserved, a copy of the instance must be
   * created using {@link #copy()}.</p>
   * @param time time of the interpolated point
   */
  void setInterpolatedTime(double time);

  /**
   * Get the state vector of the interpolated point.
   * <p>The returned vector is a reference to a reused array, so
   * it should not be modified and it should be copied if it needs
   * to be preserved across several calls.</p>
   * @return state vector at time {@link #getInterpolatedTime}
   * @see #getInterpolatedDerivatives()
   * @exception MaxCountExceededException if the number of functions evaluations is exceeded
   */
  double[] getInterpolatedState() throws MaxCountExceededException;

  /**
   * Get the derivatives of the state vector of the interpolated point.
   * <p>The returned vector is a reference to a reused array, so
   * it should not be modified and it should be copied if it needs
   * to be preserved across several calls.</p>
   * @return derivatives of the state vector at time {@link #getInterpolatedTime}
   * @see #getInterpolatedState()
   * @since 2.0
   * @exception MaxCountExceededException if the number of functions evaluations is exceeded
   */
  double[] getInterpolatedDerivatives() throws MaxCountExceededException;

  /** Get the interpolated secondary state corresponding to the secondary equations.
   * <p>The returned vector is a reference to a reused array, so
   * it should not be modified and it should be copied if it needs
   * to be preserved across several calls.</p>
   * @param index index of the secondary set, as returned by {@link
   * org.apache.commons.math3.ode.ExpandableStatefulODE#addSecondaryEquations(
   * org.apache.commons.math3.ode.SecondaryEquations)
   * ExpandableStatefulODE.addSecondaryEquations(SecondaryEquations)}
   * @return interpolated secondary state at the current interpolation date
   * @see #getInterpolatedState()
   * @see #getInterpolatedDerivatives()
   * @see #getInterpolatedSecondaryDerivatives(int)
   * @see #setInterpolatedTime(double)
   * @since 3.0
   * @exception MaxCountExceededException if the number of functions evaluations is exceeded
   */
  double[] getInterpolatedSecondaryState(int index) throws MaxCountExceededException;

  /** Get the interpolated secondary derivatives corresponding to the secondary equations.
   * <p>The returned vector is a reference to a reused array, so
   * it should not be modified and it should be copied if it needs
   * to be preserved across several calls.</p>
   * @param index index of the secondary set, as returned by {@link
   * org.apache.commons.math3.ode.ExpandableStatefulODE#addSecondaryEquations(
   * org.apache.commons.math3.ode.SecondaryEquations)
   * ExpandableStatefulODE.addSecondaryEquations(SecondaryEquations)}
   * @return interpolated secondary derivatives at the current interpolation date
   * @see #getInterpolatedState()
   * @see #getInterpolatedDerivatives()
   * @see #getInterpolatedSecondaryState(int)
   * @see #setInterpolatedTime(double)
   * @since 3.0
   * @exception MaxCountExceededException if the number of functions evaluations is exceeded
   */
  double[] getInterpolatedSecondaryDerivatives(int index) throws MaxCountExceededException;

  /** Check if the natural integration direction is forward.
   * <p>This method provides the integration direction as specified by
   * the integrator itself, it avoid some nasty problems in
   * degenerated cases like null steps due to cancellation at step
   * initialization, step control or discrete events
   * triggering.</p>
   * @return true if the integration variable (time) increases during
   * integration
   */
  boolean isForward();

  /** Copy the instance.
   * <p>The copied instance is guaranteed to be independent from the
   * original one. Both can be used with different settings for
   * interpolated time without any side effect.</p>
   * @return a deep copy of the instance, which can be used independently.
   * @see #setInterpolatedTime(double)
   * @exception MaxCountExceededException if the number of functions evaluations is exceeded
   * during step finalization
   */
   StepInterpolator copy() throws MaxCountExceededException;

}
