package optim.nonlinear.scalar;

import optim.BaseMultivariateOptimizer;
import optim.ConvergenceChecker;
import optim.OptimizationData;
import optim.PointValuePair;
import analysis.MultivariateFunction;
import exception.TooManyEvaluationsException;

public abstract class MultivariateOptimizer
    extends BaseMultivariateOptimizer<PointValuePair> {
    /** Objective function. */
    private MultivariateFunction function;
    /** Type of optimization. */
    private GoalType goal;

    /**
     * @param checker Convergence checker.
     */
    protected MultivariateOptimizer(ConvergenceChecker<PointValuePair> checker) {
        super(checker);
    }

    /**
     * {@inheritDoc}
     *
     * @param optData Optimization data. The following data will be looked for:
     * <ul>
     *  <li>{@link org.apache.commons.math3.optim.MaxEval}</li>
     *  <li>{@link org.apache.commons.math3.optim.InitialGuess}</li>
     *  <li>{@link org.apache.commons.math3.optim.SimpleBounds}</li>
     *  <li>{@link ObjectiveFunction}</li>
     *  <li>{@link GoalType}</li>
     * </ul>
     * @return {@inheritDoc}
     * @throws TooManyEvaluationsException if the maximal number of
     * evaluations is exceeded.
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
     *  <li>{@link ObjectiveFunction}</li>
     *  <li>{@link GoalType}</li>
     * </ul>
     */
    private void parseOptimizationData(OptimizationData... optData) {
        // The existing values (as set by the previous call) are reused if
        // not provided in the argument list.
        for (OptimizationData data : optData) {
            if (data instanceof GoalType) {
                goal = (GoalType) data;
                continue;
            }
            if  (data instanceof ObjectiveFunction) {
                function = ((ObjectiveFunction) data).getObjectiveFunction();
                continue;
            }
        }
    }

    /**
     * @return the optimization type.
     */
    public GoalType getGoalType() {
        return goal;
    }

    /**
     * Computes the objective function value.
     * This method <em>must</em> be called by subclasses to enforce the
     * evaluation counter limit.
     *
     * @param params Point at which the objective function must be evaluated.
     * @return the objective function value at the specified point.
     * @throws TooManyEvaluationsException if the maximal number of
     * evaluations is exceeded.
     */
    protected double computeObjectiveValue(double[] params) {
        super.incrementEvaluationCount();
        return function.value(params);
    }
}
