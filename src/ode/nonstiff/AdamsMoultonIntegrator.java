package ode.nonstiff;

import java.util.Arrays;

import linear.Array2DRowRealMatrix;
import linear.RealMatrixPreservingVisitor;
import math.util.FastMath;
import ode.ExpandableStatefulODE;
import ode.sampling.NordsieckStepInterpolator;
import exception.DimensionMismatchException;
import exception.MaxCountExceededException;
import exception.NoBracketingException;
import exception.NumberIsTooSmallException;


public class AdamsMoultonIntegrator extends AdamsIntegrator {

    /** Integrator method name. */
    private static final String METHOD_NAME = "Adams-Moulton";

    /**
     * Build an Adams-Moulton integrator with the given order and error control parameters.
     * @param nSteps number of steps of the method excluding the one being computed
     * @param minStep minimal step (sign is irrelevant, regardless of
     * integration direction, forward or backward), the last step can
     * be smaller than this
     * @param maxStep maximal step (sign is irrelevant, regardless of
     * integration direction, forward or backward), the last step can
     * be smaller than this
     * @param scalAbsoluteTolerance allowed absolute error
     * @param scalRelativeTolerance allowed relative error
     * @exception NumberIsTooSmallException if order is 1 or less
     */
    public AdamsMoultonIntegrator(final int nSteps,
                                  final double minStep, final double maxStep,
                                  final double scalAbsoluteTolerance,
                                  final double scalRelativeTolerance)
        throws NumberIsTooSmallException {
        super(METHOD_NAME, nSteps, nSteps + 1, minStep, maxStep,
              scalAbsoluteTolerance, scalRelativeTolerance);
    }

    /**
     * Build an Adams-Moulton integrator with the given order and error control parameters.
     * @param nSteps number of steps of the method excluding the one being computed
     * @param minStep minimal step (sign is irrelevant, regardless of
     * integration direction, forward or backward), the last step can
     * be smaller than this
     * @param maxStep maximal step (sign is irrelevant, regardless of
     * integration direction, forward or backward), the last step can
     * be smaller than this
     * @param vecAbsoluteTolerance allowed absolute error
     * @param vecRelativeTolerance allowed relative error
     * @exception IllegalArgumentException if order is 1 or less
     */
    public AdamsMoultonIntegrator(final int nSteps,
                                  final double minStep, final double maxStep,
                                  final double[] vecAbsoluteTolerance,
                                  final double[] vecRelativeTolerance)
        throws IllegalArgumentException {
        super(METHOD_NAME, nSteps, nSteps + 1, minStep, maxStep,
              vecAbsoluteTolerance, vecRelativeTolerance);
    }


    /** {@inheritDoc} */
    @Override
    public void integrate(final ExpandableStatefulODE equations,final double t)
        throws NumberIsTooSmallException, DimensionMismatchException,
               MaxCountExceededException, NoBracketingException {

        sanityChecks(equations, t);
        setEquations(equations);
        final boolean forward = t > equations.getTime();

        // initialize working arrays
        final double[] y0   = equations.getCompleteState();
        final double[] y    = y0.clone();
        final double[] yDot = new double[y.length];
        final double[] yTmp = new double[y.length];
        final double[] predictedScaled = new double[y.length];
        Array2DRowRealMatrix nordsieckTmp = null;

        // set up two interpolators sharing the integrator arrays
        final NordsieckStepInterpolator interpolator = new NordsieckStepInterpolator();
        interpolator.reinitialize(y, forward,
                                  equations.getPrimaryMapper(), equations.getSecondaryMappers());

        // set up integration control objects
        initIntegration(equations.getTime(), y0, t);

        // compute the initial Nordsieck vector using the configured starter integrator
        start(equations.getTime(), y, t);
        interpolator.reinitialize(stepStart, stepSize, scaled, nordsieck);
        interpolator.storeTime(stepStart);

        double hNew = stepSize;
        interpolator.rescale(hNew);

        isLastStep = false;
        do {

            double error = 10;
            while (error >= 1.0) {

                stepSize = hNew;

                // predict a first estimate of the state at step end (P in the PECE sequence)
                final double stepEnd = stepStart + stepSize;
                interpolator.setInterpolatedTime(stepEnd);
                System.arraycopy(interpolator.getInterpolatedState(), 0, yTmp, 0, y0.length);

                // evaluate a first estimate of the derivative (first E in the PECE sequence)
                computeDerivatives(stepEnd, yTmp, yDot);

                // update Nordsieck vector
                for (int j = 0; j < y0.length; ++j) {
                    predictedScaled[j] = stepSize * yDot[j];
                }
                nordsieckTmp = updateHighOrderDerivativesPhase1(nordsieck);
                updateHighOrderDerivativesPhase2(scaled, predictedScaled, nordsieckTmp);

                // apply correction (C in the PECE sequence)
                error = nordsieckTmp.walkInOptimizedOrder(new Corrector(y, predictedScaled, yTmp));

                if (error >= 1.0) {
                    // reject the step and attempt to reduce error by stepsize control
                    final double factor = computeStepGrowShrinkFactor(error);
                    hNew = filterStep(stepSize * factor, forward, false);
                    interpolator.rescale(hNew);
                }
            }

            // evaluate a final estimate of the derivative (second E in the PECE sequence)
            final double stepEnd = stepStart + stepSize;
            computeDerivatives(stepEnd, yTmp, yDot);

            // update Nordsieck vector
            final double[] correctedScaled = new double[y0.length];
            for (int j = 0; j < y0.length; ++j) {
                correctedScaled[j] = stepSize * yDot[j];
            }
            updateHighOrderDerivativesPhase2(predictedScaled, correctedScaled, nordsieckTmp);

            // discrete events handling
            System.arraycopy(yTmp, 0, y, 0, y.length);
            interpolator.reinitialize(stepEnd, stepSize, correctedScaled, nordsieckTmp);
            interpolator.storeTime(stepStart);
            interpolator.shift();
            interpolator.storeTime(stepEnd);
            stepStart = acceptStep(interpolator, y, yDot, t);
            scaled    = correctedScaled;
            nordsieck = nordsieckTmp;

            if (!isLastStep) {

                // prepare next step
                interpolator.storeTime(stepStart);

                if (resetOccurred) {
                    // some events handler has triggered changes that
                    // invalidate the derivatives, we need to restart from scratch
                    start(stepStart, y, t);
                    interpolator.reinitialize(stepStart, stepSize, scaled, nordsieck);

                }

                // stepsize control for next step
                final double  factor     = computeStepGrowShrinkFactor(error);
                final double  scaledH    = stepSize * factor;
                final double  nextT      = stepStart + scaledH;
                final boolean nextIsLast = forward ? (nextT >= t) : (nextT <= t);
                hNew = filterStep(scaledH, forward, nextIsLast);

                final double  filteredNextT      = stepStart + hNew;
                final boolean filteredNextIsLast = forward ? (filteredNextT >= t) : (filteredNextT <= t);
                if (filteredNextIsLast) {
                    hNew = t - stepStart;
                }

                interpolator.rescale(hNew);
            }

        } while (!isLastStep);

        // dispatch results
        equations.setTime(stepStart);
        equations.setCompleteState(y);

        resetInternalState();

    }

    /** Corrector for current state in Adams-Moulton method.
     * <p>
     * This visitor implements the Taylor series formula:
     * <pre>
     * Y<sub>n+1</sub> = y<sub>n</sub> + s<sub>1</sub>(n+1) + [ -1 +1 -1 +1 ... &plusmn;1 ] r<sub>n+1</sub>
     * </pre>
     * </p>
     */
    private class Corrector implements RealMatrixPreservingVisitor {

        /** Previous state. */
        private final double[] previous;

        /** Current scaled first derivative. */
        private final double[] scaled;

        /** Current state before correction. */
        private final double[] before;

        /** Current state after correction. */
        private final double[] after;

        /** Simple constructor.
         * @param previous previous state
         * @param scaled current scaled first derivative
         * @param state state to correct (will be overwritten after visit)
         */
        public Corrector(final double[] previous, final double[] scaled, final double[] state) {
            this.previous = previous;
            this.scaled   = scaled;
            this.after    = state;
            this.before   = state.clone();
        }

        /** {@inheritDoc} */
        public void start(int rows, int columns,
                          int startRow, int endRow, int startColumn, int endColumn) {
            Arrays.fill(after, 0.0);
        }

        /** {@inheritDoc} */
        public void visit(int row, int column, double value) {
            if ((row & 0x1) == 0) {
                after[column] -= value;
            } else {
                after[column] += value;
            }
        }

        /**
         * End visiting the Nordsieck vector.
         * <p>The correction is used to control stepsize. So its amplitude is
         * considered to be an error, which must be normalized according to
         * error control settings. If the normalized value is greater than 1,
         * the correction was too large and the step must be rejected.</p>
         * @return the normalized correction, if greater than 1, the step
         * must be rejected
         */
        public double end() {

            double error = 0;
            for (int i = 0; i < after.length; ++i) {
                after[i] += previous[i] + scaled[i];
                if (i < mainSetDimension) {
                    final double yScale = FastMath.max(FastMath.abs(previous[i]), FastMath.abs(after[i]));
                    final double tol = (vecAbsoluteTolerance == null) ?
                                       (scalAbsoluteTolerance + scalRelativeTolerance * yScale) :
                                       (vecAbsoluteTolerance[i] + vecRelativeTolerance[i] * yScale);
                    final double ratio  = (after[i] - before[i]) / tol;
                    error += ratio * ratio;
                }
            }

            return FastMath.sqrt(error / mainSetDimension);

        }
    }

}
