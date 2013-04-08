package optim.nonlinear.scalar;

import optim.ConvergenceChecker;
import optim.OptimizationData;
import optim.PointValuePair;
import analysis.MultivariateVectorFunction;
import exception.TooManyEvaluationsException;

public abstract class GradientMultivariateOptimizer
    extends MultivariateOptimizer {
    /**
     * Gradient of the objective function.
     */
    private MultivariateVectorFunction gradient;

    /**
     * @param checker Convergence checker.
     */
    protected GradientMultivariateOptimizer(ConvergenceChecker<PointValuePair> checker) {
        super(checker);
    }

    /**
     * Compute the gradient vector.
     *
     * @param params Point at which the gradient must be evaluated.
     * @return the gradient at the specified point.
     */
    protected double[] computeObjectiveGradient(final double[] params) {
        return gradient.value(params);
    }

    /**
     * {@inheritDoc}
     *
     * @param optData Optimization data.
     * The following data will be looked for:
     * <ul>
     *  <li>{@link org.apache.commons.math3.optim.MaxEval}</li>
     *  <li>{@link org.apache.commons.math3.optim.InitialGuess}</li>
     *  <li>{@link org.apache.commons.math3.optim.SimpleBounds}</li>
     *  <li>{@link ObjectiveFunction}</li>
     *  <li>{@link GoalType}</li>
     *  <li>{@link ObjectiveFunctionGradient}</li>
     * </ul>
     * @return {@inheritDoc}
     * @throws TooManyEvaluationsException if the maximal number of
     * evaluations (of the objective function) is exceeded.
     */
    @Override
    public PointValuePair optimize(OptimizationData... optData)
        throws TooManyEvaluationsException {
         // Retrieve settings.
        parseOptimizationData(optData);
        // Set up base class and perform computation.
        return super.optimize(optData);
    }

    /**
     * Scans the list of (required and optional) optimization data that
     * characterize the problem.
     *
     * @param optData Optimization data.
     * The following data will be looked for:
     * <ul>
     *  <li>{@link ObjectiveFunctionGradient}</li>
     * </ul>
     */
    private void parseOptimizationData(OptimizationData... optData) {
        // The existing values (as set by the previous call) are reused if
        // not provided in the argument list.
        for (OptimizationData data : optData) {
            if  (data instanceof ObjectiveFunctionGradient) {
                gradient = ((ObjectiveFunctionGradient) data).getObjectiveFunctionGradient();
                // If more data must be parsed, this statement _must_ be
                // changed to "continue".
                break;
            }
        }
    }
}
