package ode.nonstiff;

import ode.sampling.StepInterpolator;


class ThreeEighthesStepInterpolator
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
  public ThreeEighthesStepInterpolator() {
  }

  /** Copy constructor.
   * @param interpolator interpolator to copy from. The copy is a deep
   * copy: its arrays are separated from the original arrays of the
   * instance
   */
  public ThreeEighthesStepInterpolator(final ThreeEighthesStepInterpolator interpolator) {
    super(interpolator);
  }

  /** {@inheritDoc} */
  @Override
  protected StepInterpolator doCopy() {
    return new ThreeEighthesStepInterpolator(this);
  }


  /** {@inheritDoc} */
  @Override
  protected void computeInterpolatedStateAndDerivatives(final double theta,
                                          final double oneMinusThetaH) {

      final double coeffDot3  = 0.75 * theta;
      final double coeffDot1  = coeffDot3 * (4 * theta - 5) + 1;
      final double coeffDot2  = coeffDot3 * (5 - 6 * theta);
      final double coeffDot4  = coeffDot3 * (2 * theta - 1);

      if ((previousState != null) && (theta <= 0.5)) {
          final double s          = theta * h / 8.0;
          final double fourTheta2 = 4 * theta * theta;
          final double coeff1     = s * (8 - 15 * theta + 2 * fourTheta2);
          final double coeff2     = 3 * s * (5 * theta - fourTheta2);
          final double coeff3     = 3 * s * theta;
          final double coeff4     = s * (-3 * theta + fourTheta2);
          for (int i = 0; i < interpolatedState.length; ++i) {
              final double yDot1 = yDotK[0][i];
              final double yDot2 = yDotK[1][i];
              final double yDot3 = yDotK[2][i];
              final double yDot4 = yDotK[3][i];
              interpolatedState[i] =
                      previousState[i] + coeff1 * yDot1 + coeff2 * yDot2 + coeff3 * yDot3 + coeff4 * yDot4;
              interpolatedDerivatives[i] =
                      coeffDot1 * yDot1 + coeffDot2 * yDot2 + coeffDot3 * yDot3 + coeffDot4 * yDot4;

          }
      } else {
          final double s          = oneMinusThetaH / 8.0;
          final double fourTheta2 = 4 * theta * theta;
          final double coeff1     = s * (1 - 7 * theta + 2 * fourTheta2);
          final double coeff2     = 3 * s * (1 + theta - fourTheta2);
          final double coeff3     = 3 * s * (1 + theta);
          final double coeff4     = s * (1 + theta + fourTheta2);
          for (int i = 0; i < interpolatedState.length; ++i) {
              final double yDot1 = yDotK[0][i];
              final double yDot2 = yDotK[1][i];
              final double yDot3 = yDotK[2][i];
              final double yDot4 = yDotK[3][i];
              interpolatedState[i] =
                      currentState[i] - coeff1 * yDot1 - coeff2 * yDot2 - coeff3 * yDot3 - coeff4 * yDot4;
              interpolatedDerivatives[i] =
                      coeffDot1 * yDot1 + coeffDot2 * yDot2 + coeffDot3 * yDot3 + coeffDot4 * yDot4;

          }
      }

  }

}
