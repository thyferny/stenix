package ode.nonstiff;

import math.util.FastMath;
import ode.sampling.StepInterpolator;


class GillStepInterpolator
  extends RungeKuttaStepInterpolator {

    /** First Gill coefficient. */
    private static final double ONE_MINUS_INV_SQRT_2 = 1 - FastMath.sqrt(0.5);

    /** Second Gill coefficient. */
    private static final double ONE_PLUS_INV_SQRT_2 = 1 + FastMath.sqrt(0.5);

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
  public GillStepInterpolator() {
  }

  /** Copy constructor.
   * @param interpolator interpolator to copy from. The copy is a deep
   * copy: its arrays are separated from the original arrays of the
   * instance
   */
  public GillStepInterpolator(final GillStepInterpolator interpolator) {
    super(interpolator);
  }

  /** {@inheritDoc} */
  @Override
  protected StepInterpolator doCopy() {
    return new GillStepInterpolator(this);
  }


  /** {@inheritDoc} */
  @Override
  protected void computeInterpolatedStateAndDerivatives(final double theta,
                                          final double oneMinusThetaH) {

    final double twoTheta   = 2 * theta;
    final double fourTheta2 = twoTheta * twoTheta;
    final double coeffDot1  = theta * (twoTheta - 3) + 1;
    final double cDot23     = twoTheta * (1 - theta);
    final double coeffDot2  = cDot23  * ONE_MINUS_INV_SQRT_2;
    final double coeffDot3  = cDot23  * ONE_PLUS_INV_SQRT_2;
    final double coeffDot4  = theta * (twoTheta - 1);

    if ((previousState != null) && (theta <= 0.5)) {
        final double s         = theta * h / 6.0;
        final double c23       = s * (6 * theta - fourTheta2);
        final double coeff1    = s * (6 - 9 * theta + fourTheta2);
        final double coeff2    = c23  * ONE_MINUS_INV_SQRT_2;
        final double coeff3    = c23  * ONE_PLUS_INV_SQRT_2;
        final double coeff4    = s * (-3 * theta + fourTheta2);
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
        final double s      = oneMinusThetaH / 6.0;
        final double c23    = s * (2 + twoTheta - fourTheta2);
        final double coeff1 = s * (1 - 5 * theta + fourTheta2);
        final double coeff2 = c23  * ONE_MINUS_INV_SQRT_2;
        final double coeff3 = c23  * ONE_PLUS_INV_SQRT_2;
        final double coeff4 = s * (1 + theta + fourTheta2);
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
