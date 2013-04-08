package ode.sampling;

public class DummyStepHandler implements StepHandler {

    /** Private constructor.
     * The constructor is private to prevent users from creating
     * instances (Singleton design-pattern).
     */
    private DummyStepHandler() {
    }

    /** Get the only instance.
     * @return the only instance
     */
    public static DummyStepHandler getInstance() {
        return LazyHolder.INSTANCE;
    }

    /** {@inheritDoc} */
    public void init(double t0, double[] y0, double t) {
    }

    /**
     * Handle the last accepted step.
     * This method does nothing in this class.
     * @param interpolator interpolator for the last accepted step. For
     * efficiency purposes, the various integrators reuse the same
     * object on each call, so if the instance wants to keep it across
     * all calls (for example to provide at the end of the integration a
     * continuous model valid throughout the integration range), it
     * should build a local copy using the clone method and store this
     * copy.
     * @param isLast true if the step is the last one
     */
    public void handleStep(final StepInterpolator interpolator, final boolean isLast) {
    }

    // CHECKSTYLE: stop HideUtilityClassConstructor
    /** Holder for the instance.
     * <p>We use here the Initialization On Demand Holder Idiom.</p>
     */
    private static class LazyHolder {
        /** Cached field instance. */
        private static final DummyStepHandler INSTANCE = new DummyStepHandler();
    }
    // CHECKSTYLE: resume HideUtilityClassConstructor

    /** Handle deserialization of the singleton.
     * @return the singleton instance
     */
    private Object readResolve() {
        // return the singleton instance
        return LazyHolder.INSTANCE;
    }

}
