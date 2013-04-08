package ode.nonstiff;

import ode.sampling.StepInterpolator;

class EulerStepInterpolator
  extends RungeKuttaStepInterpolator {

  /** Serializable version identifier. */
  private static final long serialVersionUID = 20111120L;

  /** Simple constructor.
   * This constructor builds an instance that is not usable yet, the
   * {@link
   * org.apache.commons.math3.ode.sampling.AbstractStepInterpolator#reinitialize}
   * method should be called before using the instance in order to
   * initialize the internal arrays. This constructor is used only
   * in order to delay the initialization in some cases. The {@link
   * RungeKuttaIntegrator} class uses the prototyping design pattern
   * to create the step interpolators by cloning an uninitialized model
   * and later initializing the copy.
   */
  public EulerStepInterpolator() {
  }

  /** Copy constructor.
   * @param interpolator interpolator to copy from. The copy is a deep
   * copy: its arrays are separated from the original arrays of the
   * instance
   */
  public EulerStepInterpolator(final EulerStepInterpolator interpolator) {
    super(interpolator);
  }

  /** {@inheritDoc} */
  @Override
  protected StepInterpolator doCopy() {
    return new EulerStepInterpolator(this);
  }


  /** {@inheritDoc} */
  @Override
  protected void computeInterpolatedStateAndDerivatives(final double theta,
                                          final double oneMinusThetaH) {
      if ((previousState != null) && (theta <= 0.5)) {
          for (int i = 0; i < interpolatedState.length; ++i) {
              interpolatedState[i] = previousState[i] + theta * h * yDotK[0][i];
          }
          System.arraycopy(yDotK[0], 0, interpolatedDerivatives, 0, interpolatedDerivatives.length);
      } else {
          for (int i = 0; i < interpolatedState.length; ++i) {
              interpolatedState[i] = currentState[i] - oneMinusThetaH * yDotK[0][i];
          }
          System.arraycopy(yDotK[0], 0, interpolatedDerivatives, 0, interpolatedDerivatives.length);
      }

  }

}
