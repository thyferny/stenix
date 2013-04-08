package ode.sampling;

import exception.MaxCountExceededException;

public interface StepHandler {

    /** Initialize step handler at the start of an ODE integration.
     * <p>
     * This method is called once at the start of the integration. It
     * may be used by the step handler to initialize some internal data
     * if needed.
     * </p>
     * @param t0 start value of the independent <i>time</i> variable
     * @param y0 array containing the start value of the state vector
     * @param t target time for the integration
     */
    void init(double t0, double[] y0, double t);

    /**
     * Handle the last accepted step
     * @param interpolator interpolator for the last accepted step. For
     * efficiency purposes, the various integrators reuse the same
     * object on each call, so if the instance wants to keep it across
     * all calls (for example to provide at the end of the integration a
     * continuous model valid throughout the integration range, as the
     * {@link org.apache.commons.math3.ode.ContinuousOutputModel
     * ContinuousOutputModel} class does), it should build a local copy
     * using the clone method of the interpolator and store this copy.
     * Keeping only a reference to the interpolator and reusing it will
     * result in unpredictable behavior (potentially crashing the application).
     * @param isLast true if the step is the last one
     * @exception MaxCountExceededException if the interpolator throws one because
     * the number of functions evaluations is exceeded
     */
    void handleStep(StepInterpolator interpolator, boolean isLast)
        throws MaxCountExceededException;

}
