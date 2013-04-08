package ode.nonstiff;

import ode.sampling.StepInterpolator;


class MidpointStepInterpolator
  extends RungeKuttaStepInterpolator {

  /** Serializable version identifier */
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
  public MidpointStepInterpolator() {
  }

  /** Copy constructor.
   * @param interpolator interpolator to copy from. The copy is a deep
   * copy: its arrays are separated from the original arrays of the
   * instance
   */
  public MidpointStepInterpolator(final MidpointStepInterpolator interpolator) {
    super(interpolator);
  }

  /** {@inheritDoc} */
  @Override
  protected StepInterpolator doCopy() {
    return new MidpointStepInterpolator(this);
  }


  /** {@inheritDoc} */
  @Override
  protected void computeInterpolatedStateAndDerivatives(final double theta,
                                          final double oneMinusThetaH) {

    final double coeffDot2 = 2 * theta;
    final double coeffDot1 = 1 - coeffDot2;

    if ((previousState != null) && (theta <= 0.5)) {
        final double coeff1    = theta * oneMinusThetaH;
        final double coeff2    = theta * theta * h;
        for (int i = 0; i < interpolatedState.length; ++i) {
            final double yDot1 = yDotK[0][i];
            final double yDot2 = yDotK[1][i];
            interpolatedState[i] = previousState[i] + coeff1 * yDot1 + coeff2 * yDot2;
            interpolatedDerivatives[i] = coeffDot1 * yDot1 + coeffDot2 * yDot2;
        }
    } else {
        final double coeff1    = oneMinusThetaH * theta;
        final double coeff2    = oneMinusThetaH * (1.0 + theta);
        for (int i = 0; i < interpolatedState.length; ++i) {
            final double yDot1 = yDotK[0][i];
            final double yDot2 = yDotK[1][i];
            interpolatedState[i] = currentState[i] + coeff1 * yDot1 - coeff2 * yDot2;
            interpolatedDerivatives[i] = coeffDot1 * yDot1 + coeffDot2 * yDot2;
        }
    }

  }

}
