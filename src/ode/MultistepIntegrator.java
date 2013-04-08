package ode;

import linear.Array2DRowRealMatrix;
import math.util.FastMath;
import ode.nonstiff.AdaptiveStepsizeIntegrator;
import ode.nonstiff.DormandPrince853Integrator;
import ode.sampling.StepHandler;
import ode.sampling.StepInterpolator;
import exception.DimensionMismatchException;
import exception.MaxCountExceededException;
import exception.NoBracketingException;
import exception.NumberIsTooSmallException;
import exception.util.LocalizedFormats;


public abstract class MultistepIntegrator extends AdaptiveStepsizeIntegrator {

    /** First scaled derivative (h y'). */
    protected double[] scaled;

    /** Nordsieck matrix of the higher scaled derivatives.
     * <p>(h<sup>2</sup>/2 y'', h<sup>3</sup>/6 y''' ..., h<sup>k</sup>/k! y<sup>(k)</sup>)</p>
     */
    protected Array2DRowRealMatrix nordsieck;

    /** Starter integrator. */
    private FirstOrderIntegrator starter;

    /** Number of steps of the multistep method (excluding the one being computed). */
    private final int nSteps;

    /** Stepsize control exponent. */
    private double exp;

    /** Safety factor for stepsize control. */
    private double safety;

    /** Minimal reduction factor for stepsize control. */
    private double minReduction;

    /** Maximal growth factor for stepsize control. */
    private double maxGrowth;

    /**
     * Build a multistep integrator with the given stepsize bounds.
     * <p>The default starter integrator is set to the {@link
     * DormandPrince853Integrator Dormand-Prince 8(5,3)} integrator with
     * some defaults settings.</p>
     * <p>
     * The default max growth factor is set to a quite low value: 2<sup>1/order</sup>.
     * </p>
     * @param name name of the method
     * @param nSteps number of steps of the multistep method
     * (excluding the one being computed)
     * @param order order of the method
     * @param minStep minimal step (must be positive even for backward
     * integration), the last step can be smaller than this
     * @param maxStep maximal step (must be positive even for backward
     * integration)
     * @param scalAbsoluteTolerance allowed absolute error
     * @param scalRelativeTolerance allowed relative error
     * @exception NumberIsTooSmallException if number of steps is smaller than 2
     */
    protected MultistepIntegrator(final String name, final int nSteps,
                                  final int order,
                                  final double minStep, final double maxStep,
                                  final double scalAbsoluteTolerance,
                                  final double scalRelativeTolerance)
        throws NumberIsTooSmallException {

        super(name, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);

        if (nSteps < 2) {
            throw new NumberIsTooSmallException(
                  LocalizedFormats.INTEGRATION_METHOD_NEEDS_AT_LEAST_TWO_PREVIOUS_POINTS,
                  nSteps, 2, true);
        }

        starter = new DormandPrince853Integrator(minStep, maxStep,
                                                 scalAbsoluteTolerance,
                                                 scalRelativeTolerance);
        this.nSteps = nSteps;

        exp = -1.0 / order;

        // set the default values of the algorithm control parameters
        setSafety(0.9);
        setMinReduction(0.2);
        setMaxGrowth(FastMath.pow(2.0, -exp));

    }

    /**
     * Build a multistep integrator with the given stepsize bounds.
     * <p>The default starter integrator is set to the {@link
     * DormandPrince853Integrator Dormand-Prince 8(5,3)} integrator with
     * some defaults settings.</p>
     * <p>
     * The default max growth factor is set to a quite low value: 2<sup>1/order</sup>.
     * </p>
     * @param name name of the method
     * @param nSteps number of steps of the multistep method
     * (excluding the one being computed)
     * @param order order of the method
     * @param minStep minimal step (must be positive even for backward
     * integration), the last step can be smaller than this
     * @param maxStep maximal step (must be positive even for backward
     * integration)
     * @param vecAbsoluteTolerance allowed absolute error
     * @param vecRelativeTolerance allowed relative error
     */
    protected MultistepIntegrator(final String name, final int nSteps,
                                  final int order,
                                  final double minStep, final double maxStep,
                                  final double[] vecAbsoluteTolerance,
                                  final double[] vecRelativeTolerance) {
        super(name, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        starter = new DormandPrince853Integrator(minStep, maxStep,
                                                 vecAbsoluteTolerance,
                                                 vecRelativeTolerance);
        this.nSteps = nSteps;

        exp = -1.0 / order;

        // set the default values of the algorithm control parameters
        setSafety(0.9);
        setMinReduction(0.2);
        setMaxGrowth(FastMath.pow(2.0, -exp));

    }

    /**
     * Get the starter integrator.
     * @return starter integrator
     */
    public ODEIntegrator getStarterIntegrator() {
        return starter;
    }

    /**
     * Set the starter integrator.
     * <p>The various step and event handlers for this starter integrator
     * will be managed automatically by the multi-step integrator. Any
     * user configuration for these elements will be cleared before use.</p>
     * @param starterIntegrator starter integrator
     */
    public void setStarterIntegrator(FirstOrderIntegrator starterIntegrator) {
        this.starter = starterIntegrator;
    }

    /** Start the integration.
     * <p>This method computes one step using the underlying starter integrator,
     * and initializes the Nordsieck vector at step start. The starter integrator
     * purpose is only to establish initial conditions, it does not really change
     * time by itself. The top level multistep integrator remains in charge of
     * handling time propagation and events handling as it will starts its own
     * computation right from the beginning. In a sense, the starter integrator
     * can be seen as a dummy one and so it will never trigger any user event nor
     * call any user step handler.</p>
     * @param t0 initial time
     * @param y0 initial value of the state vector at t0
     * @param t target time for the integration
     * (can be set to a value smaller than <code>t0</code> for backward integration)
     * @exception DimensionMismatchException if arrays dimension do not match equations settings
     * @exception NumberIsTooSmallException if integration step is too small
     * @exception MaxCountExceededException if the number of functions evaluations is exceeded
     * @exception NoBracketingException if the location of an event cannot be bracketed
     */
    protected void start(final double t0, final double[] y0, final double t)
        throws DimensionMismatchException, NumberIsTooSmallException,
               MaxCountExceededException, NoBracketingException {

        // make sure NO user event nor user step handler is triggered,
        // this is the task of the top level integrator, not the task
        // of the starter integrator
        starter.clearEventHandlers();
        starter.clearStepHandlers();

        // set up one specific step handler to extract initial Nordsieck vector
        starter.addStepHandler(new NordsieckInitializer(nSteps, y0.length));

        // start integration, expecting a InitializationCompletedMarkerException
        try {
            starter.integrate(new CountingDifferentialEquations(y0.length),
                              t0, y0, t, new double[y0.length]);
        } catch (InitializationCompletedMarkerException icme) { // NOPMD
            // this is the expected nominal interruption of the start integrator
        }

        // remove the specific step handler
        starter.clearStepHandlers();

    }

    /** Initialize the high order scaled derivatives at step start.
     * @param h step size to use for scaling
     * @param t first steps times
     * @param y first steps states
     * @param yDot first steps derivatives
     * @return Nordieck vector at first step (h<sup>2</sup>/2 y''<sub>n</sub>,
     * h<sup>3</sup>/6 y'''<sub>n</sub> ... h<sup>k</sup>/k! y<sup>(k)</sup><sub>n</sub>)
     */
    protected abstract Array2DRowRealMatrix initializeHighOrderDerivatives(final double h, final double[] t,
                                                                           final double[][] y,
                                                                           final double[][] yDot);

    /** Get the minimal reduction factor for stepsize control.
     * @return minimal reduction factor
     */
    public double getMinReduction() {
        return minReduction;
    }

    /** Set the minimal reduction factor for stepsize control.
     * @param minReduction minimal reduction factor
     */
    public void setMinReduction(final double minReduction) {
        this.minReduction = minReduction;
    }

    /** Get the maximal growth factor for stepsize control.
     * @return maximal growth factor
     */
    public double getMaxGrowth() {
        return maxGrowth;
    }

    /** Set the maximal growth factor for stepsize control.
     * @param maxGrowth maximal growth factor
     */
    public void setMaxGrowth(final double maxGrowth) {
        this.maxGrowth = maxGrowth;
    }

    /** Get the safety factor for stepsize control.
     * @return safety factor
     */
    public double getSafety() {
      return safety;
    }

    /** Set the safety factor for stepsize control.
     * @param safety safety factor
     */
    public void setSafety(final double safety) {
      this.safety = safety;
    }

    /** Compute step grow/shrink factor according to normalized error.
     * @param error normalized error of the current step
     * @return grow/shrink factor for next step
     */
    protected double computeStepGrowShrinkFactor(final double error) {
        return FastMath.min(maxGrowth, FastMath.max(minReduction, safety * FastMath.pow(error, exp)));
    }

    /** Transformer used to convert the first step to Nordsieck representation. */
    public interface NordsieckTransformer {
        /** Initialize the high order scaled derivatives at step start.
         * @param h step size to use for scaling
         * @param t first steps times
         * @param y first steps states
         * @param yDot first steps derivatives
         * @return Nordieck vector at first step (h<sup>2</sup>/2 y''<sub>n</sub>,
         * h<sup>3</sup>/6 y'''<sub>n</sub> ... h<sup>k</sup>/k! y<sup>(k)</sup><sub>n</sub>)
         */
        Array2DRowRealMatrix initializeHighOrderDerivatives(final double h, final double[] t,
                                                            final double[][] y,
                                                            final double[][] yDot);
    }

    /** Specialized step handler storing the first step. */
    private class NordsieckInitializer implements StepHandler {

        /** Steps counter. */
        private int count;

        /** First steps times. */
        private final double[] t;

        /** First steps states. */
        private final double[][] y;

        /** First steps derivatives. */
        private final double[][] yDot;

        /** Simple constructor.
         * @param nSteps number of steps of the multistep method (excluding the one being computed)
         * @param n problem dimension
         */
        public NordsieckInitializer(final int nSteps, final int n) {
            this.count = 0;
            this.t     = new double[nSteps];
            this.y     = new double[nSteps][n];
            this.yDot  = new double[nSteps][n];
        }

        /** {@inheritDoc} */
        public void handleStep(StepInterpolator interpolator, boolean isLast)
            throws MaxCountExceededException {

            final double prev = interpolator.getPreviousTime();
            final double curr = interpolator.getCurrentTime();

            if (count == 0) {
                // first step, we need to store also the beginning of the step
                interpolator.setInterpolatedTime(prev);
                t[0] = prev;
                System.arraycopy(interpolator.getInterpolatedState(), 0,
                                 y[0],    0, y[0].length);
                System.arraycopy(interpolator.getInterpolatedDerivatives(), 0,
                                 yDot[0], 0, yDot[0].length);
            }

            // store the end of the step
            ++count;
            interpolator.setInterpolatedTime(curr);
            t[count] = curr;
            System.arraycopy(interpolator.getInterpolatedState(), 0,
                             y[count],    0, y[count].length);
            System.arraycopy(interpolator.getInterpolatedDerivatives(), 0,
                             yDot[count], 0, yDot[count].length);

            if (count == t.length - 1) {

                // this was the last step we needed, we can compute the derivatives
                stepStart = t[0];
                stepSize  = (t[t.length - 1] - t[0]) / (t.length - 1);

                // first scaled derivative
                scaled = yDot[0].clone();
                for (int j = 0; j < scaled.length; ++j) {
                    scaled[j] *= stepSize;
                }

                // higher order derivatives
                nordsieck = initializeHighOrderDerivatives(stepSize, t, y, yDot);

                // stop the integrator now that all needed steps have been handled
                throw new InitializationCompletedMarkerException();

            }

        }

        /** {@inheritDoc} */
        public void init(double t0, double[] y0, double time) {
            // nothing to do
        }

    }

    /** Marker exception used ONLY to stop the starter integrator after first step. */
    private static class InitializationCompletedMarkerException
        extends RuntimeException {

        /** Serializable version identifier. */
        private static final long serialVersionUID = -1914085471038046418L;

        /** Simple constructor. */
        public InitializationCompletedMarkerException() {
            super((Throwable) null);
        }

    }

    /** Wrapper for differential equations, ensuring start evaluations are counted. */
    private class CountingDifferentialEquations implements FirstOrderDifferentialEquations {

        /** Dimension of the problem. */
        private final int dimension;

        /** Simple constructor.
         * @param dimension dimension of the problem
         */
        public CountingDifferentialEquations(final int dimension) {
            this.dimension = dimension;
        }

        /** {@inheritDoc} */
        public void computeDerivatives(double t, double[] y, double[] dot)
            throws MaxCountExceededException, DimensionMismatchException {
            MultistepIntegrator.this.computeDerivatives(t, y, dot);
        }

        /** {@inheritDoc} */
        public int getDimension() {
            return dimension;
        }

    }

}
