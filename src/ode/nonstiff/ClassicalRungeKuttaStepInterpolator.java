package ode.nonstiff;

import ode.sampling.StepInterpolator;

class ClassicalRungeKuttaStepInterpolator
    extends RungeKuttaStepInterpolator {

    /** Serializable version identifier. */
    private static final long serialVersionUID = 20111120L;

    /** Simple constructor.
     * This constructor builds an instance that is not usable yet, the
     * {@link RungeKuttaStepInterpolator#reinitialize} method should be
     * called before using the instance in order to initialize the
     * internal arrays. This constructor is used only in order to delay
     * the initialization in some cases. The {@link RungeKuttaIntegrator}
     * class uses the prototyping design pattern to create the step
     * interpolators by cloning an uninitialized model and latter initializing
     * the copy.
     */
    public ClassicalRungeKuttaStepInterpolator() {
    }

    /** Copy constructor.
     * @param interpolator interpolator to copy from. The copy is a deep
     * copy: its arrays are separated from the original arrays of the
     * instance
     */
    public ClassicalRungeKuttaStepInterpolator(final ClassicalRungeKuttaStepInterpolator interpolator) {
        super(interpolator);
    }

    /** {@inheritDoc} */
    @Override
    protected StepInterpolator doCopy() {
        return new ClassicalRungeKuttaStepInterpolator(this);
    }

    /** {@inheritDoc} */
    @Override
    protected void computeInterpolatedStateAndDerivatives(final double theta,
                                            final double oneMinusThetaH) {

        final double oneMinusTheta  = 1 - theta;
        final double oneMinus2Theta = 1 - 2 * theta;
        final double coeffDot1     = oneMinusTheta * oneMinus2Theta;
        final double coeffDot23    = 2 * theta * oneMinusTheta;
        final double coeffDot4     = -theta * oneMinus2Theta;
        if ((previousState != null) && (theta <= 0.5)) {
            final double fourTheta2     = 4 * theta * theta;
            final double s             = theta * h / 6.0;
            final double coeff1        = s * ( 6 - 9 * theta + fourTheta2);
            final double coeff23       = s * ( 6 * theta - fourTheta2);
            final double coeff4        = s * (-3 * theta + fourTheta2);
            for (int i = 0; i < interpolatedState.length; ++i) {
                final double yDot1  = yDotK[0][i];
                final double yDot23 = yDotK[1][i] + yDotK[2][i];
                final double yDot4  = yDotK[3][i];
                interpolatedState[i] =
                        previousState[i] + coeff1  * yDot1 + coeff23 * yDot23 + coeff4  * yDot4;
                interpolatedDerivatives[i] =
                        coeffDot1 * yDot1 + coeffDot23 * yDot23 + coeffDot4 * yDot4;
            }
        } else {
            final double fourTheta      = 4 * theta;
            final double s             = oneMinusThetaH / 6.0;
            final double coeff1        = s * ((-fourTheta + 5) * theta - 1);
            final double coeff23       = s * (( fourTheta - 2) * theta - 2);
            final double coeff4        = s * ((-fourTheta - 1) * theta - 1);
            for (int i = 0; i < interpolatedState.length; ++i) {
                final double yDot1  = yDotK[0][i];
                final double yDot23 = yDotK[1][i] + yDotK[2][i];
                final double yDot4  = yDotK[3][i];
                interpolatedState[i] =
                        currentState[i] + coeff1  * yDot1 + coeff23 * yDot23 + coeff4  * yDot4;
                interpolatedDerivatives[i] =
                        coeffDot1 * yDot1 + coeffDot23 * yDot23 + coeffDot4 * yDot4;
            }
        }

    }

}
