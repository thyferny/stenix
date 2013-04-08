package ode.sampling;

import math.util.FastMath;
import math.util.Precision;
import exception.MaxCountExceededException;

public class StepNormalizer implements StepHandler {
    /** Fixed time step. */
    private double h;

    /** Underlying step handler. */
    private final FixedStepHandler handler;

    /** First step time. */
    private double firstTime;

    /** Last step time. */
    private double lastTime;

    /** Last state vector. */
    private double[] lastState;

    /** Last derivatives vector. */
    private double[] lastDerivatives;

    /** Integration direction indicator. */
    private boolean forward;

    /** The step normalizer bounds settings to use. */
    private final StepNormalizerBounds bounds;

    /** The step normalizer mode to use. */
    private final StepNormalizerMode mode;

    /** Simple constructor. Uses {@link StepNormalizerMode#INCREMENT INCREMENT}
     * mode, and {@link StepNormalizerBounds#FIRST FIRST} bounds setting, for
     * backwards compatibility.
     * @param h fixed time step (sign is not used)
     * @param handler fixed time step handler to wrap
     */
    public StepNormalizer(final double h, final FixedStepHandler handler) {
        this(h, handler, StepNormalizerMode.INCREMENT,
             StepNormalizerBounds.FIRST);
    }

    /** Simple constructor. Uses {@link StepNormalizerBounds#FIRST FIRST}
     * bounds setting.
     * @param h fixed time step (sign is not used)
     * @param handler fixed time step handler to wrap
     * @param mode step normalizer mode to use
     * @since 3.0
     */
    public StepNormalizer(final double h, final FixedStepHandler handler,
                          final StepNormalizerMode mode) {
        this(h, handler, mode, StepNormalizerBounds.FIRST);
    }

    /** Simple constructor. Uses {@link StepNormalizerMode#INCREMENT INCREMENT}
     * mode.
     * @param h fixed time step (sign is not used)
     * @param handler fixed time step handler to wrap
     * @param bounds step normalizer bounds setting to use
     * @since 3.0
     */
    public StepNormalizer(final double h, final FixedStepHandler handler,
                          final StepNormalizerBounds bounds) {
        this(h, handler, StepNormalizerMode.INCREMENT, bounds);
    }

    /** Simple constructor.
     * @param h fixed time step (sign is not used)
     * @param handler fixed time step handler to wrap
     * @param mode step normalizer mode to use
     * @param bounds step normalizer bounds setting to use
     * @since 3.0
     */
    public StepNormalizer(final double h, final FixedStepHandler handler,
                          final StepNormalizerMode mode,
                          final StepNormalizerBounds bounds) {
        this.h          = FastMath.abs(h);
        this.handler    = handler;
        this.mode       = mode;
        this.bounds     = bounds;
        firstTime       = Double.NaN;
        lastTime        = Double.NaN;
        lastState       = null;
        lastDerivatives = null;
        forward         = true;
    }

    /** {@inheritDoc} */
    public void init(double t0, double[] y0, double t) {

        firstTime       = Double.NaN;
        lastTime        = Double.NaN;
        lastState       = null;
        lastDerivatives = null;
        forward         = true;

        // initialize the underlying handler
        handler.init(t0, y0, t);

    }

    /**
     * Handle the last accepted step
     * @param interpolator interpolator for the last accepted step. For
     * efficiency purposes, the various integrators reuse the same
     * object on each call, so if the instance wants to keep it across
     * all calls (for example to provide at the end of the integration a
     * continuous model valid throughout the integration range), it
     * should build a local copy using the clone method and store this
     * copy.
     * @param isLast true if the step is the last one
     * @exception MaxCountExceededException if the interpolator throws one because
     * the number of functions evaluations is exceeded
     */
    public void handleStep(final StepInterpolator interpolator, final boolean isLast)
        throws MaxCountExceededException {
        // The first time, update the last state with the start information.
        if (lastState == null) {
            firstTime = interpolator.getPreviousTime();
            lastTime = interpolator.getPreviousTime();
            interpolator.setInterpolatedTime(lastTime);
            lastState = interpolator.getInterpolatedState().clone();
            lastDerivatives = interpolator.getInterpolatedDerivatives().clone();

            // Take the integration direction into account.
            forward = interpolator.getCurrentTime() >= lastTime;
            if (!forward) {
                h = -h;
            }
        }

        // Calculate next normalized step time.
        double nextTime = (mode == StepNormalizerMode.INCREMENT) ?
                          lastTime + h :
                          (FastMath.floor(lastTime / h) + 1) * h;
        if (mode == StepNormalizerMode.MULTIPLES &&
            Precision.equals(nextTime, lastTime, 1)) {
            nextTime += h;
        }

        // Process normalized steps as long as they are in the current step.
        boolean nextInStep = isNextInStep(nextTime, interpolator);
        while (nextInStep) {
            // Output the stored previous step.
            doNormalizedStep(false);

            // Store the next step as last step.
            storeStep(interpolator, nextTime);

            // Move on to the next step.
            nextTime += h;
            nextInStep = isNextInStep(nextTime, interpolator);
        }

        if (isLast) {
            // There will be no more steps. The stored one should be given to
            // the handler. We may have to output one more step. Only the last
            // one of those should be flagged as being the last.
            boolean addLast = bounds.lastIncluded() &&
                              lastTime != interpolator.getCurrentTime();
            doNormalizedStep(!addLast);
            if (addLast) {
                storeStep(interpolator, interpolator.getCurrentTime());
                doNormalizedStep(true);
            }
        }
    }

    /**
     * Returns a value indicating whether the next normalized time is in the
     * current step.
     * @param nextTime the next normalized time
     * @param interpolator interpolator for the last accepted step, to use to
     * get the end time of the current step
     * @return value indicating whether the next normalized time is in the
     * current step
     */
    private boolean isNextInStep(double nextTime,
                                 StepInterpolator interpolator) {
        return forward ?
               nextTime <= interpolator.getCurrentTime() :
               nextTime >= interpolator.getCurrentTime();
    }

    /**
     * Invokes the underlying step handler for the current normalized step.
     * @param isLast true if the step is the last one
     */
    private void doNormalizedStep(boolean isLast) {
        if (!bounds.firstIncluded() && firstTime == lastTime) {
            return;
        }
        handler.handleStep(lastTime, lastState, lastDerivatives, isLast);
    }

    /** Stores the interpolated information for the given time in the current
     * state.
     * @param interpolator interpolator for the last accepted step, to use to
     * get the interpolated information
     * @param t the time for which to store the interpolated information
     * @exception MaxCountExceededException if the interpolator throws one because
     * the number of functions evaluations is exceeded
     */
    private void storeStep(StepInterpolator interpolator, double t)
        throws MaxCountExceededException {
        lastTime = t;
        interpolator.setInterpolatedTime(lastTime);
        System.arraycopy(interpolator.getInterpolatedState(), 0,
                         lastState, 0, lastState.length);
        System.arraycopy(interpolator.getInterpolatedDerivatives(), 0,
                         lastDerivatives, 0, lastDerivatives.length);
    }
}
