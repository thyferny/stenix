package optim.univariate;

import optim.BaseOptimizer;
import optim.ConvergenceChecker;
import optim.OptimizationData;
import optim.nonlinear.scalar.GoalType;
import analysis.UnivariateFunction;
import exception.TooManyEvaluationsException;

public abstract class UnivariateOptimizer
    extends BaseOptimizer<UnivariatePointValuePair> {
    /** Objective function. */
    private UnivariateFunction function;
    /** Type of optimization. */
    private GoalType goal;
    /** Initial guess. */
    private double start;
    /** Lower bound. */
    private double min;
    /** Upper bound. */
    private double max;

    /**
     * @param checker Convergence checker.
     */
    protected UnivariateOptimizer(ConvergenceChecker<UnivariatePointValuePair> checker) {
        super(checker);
    }

    /**
     * {@inheritDoc}
     *
     * @param optData Optimization data.
     * The following data will be looked for:
     * <ul>
     *  <li>{@link GoalType}</li>
     *  <li>{@link SearchInterval}</li>
     *  <li>{@link UnivariateObjectiveFunction}</li>
     * </ul>
     * @return {@inheritDoc}
     * @throws TooManyEvaluationsException if the maximal number of
     * evaluations is exceeded.
     */
    public UnivariatePointValuePair optimize(OptimizationData... optData)
        throws TooManyEvaluationsException {
        // Retrieve settings.
        parseOptimizationData(optData);
        // Perform computation.
        return super.optimize(optData);
    }

    /**
     * @return the optimization type.
     */
    public GoalType getGoalType() {
        return goal;
    }

    /**
     * Scans the list of (required and optional) optimization data that
     * characterize the problem.
     *
     * @param optData Optimization data.
     * The following data will be looked for:
     * <ul>
     *  <li>{@link GoalType}</li>
     *  <li>{@link SearchInterval}</li>
     *  <li>{@link UnivariateObjectiveFunction}</li>
     * </ul>
     */
    private void parseOptimizationData(OptimizationData... optData) {
        // The existing values (as set by the previous call) are reused if
        // not provided in the argument list.
        for (OptimizationData data : optData) {
            if (data instanceof SearchInterval) {
                final SearchInterval interval = (SearchInterval) data;
                min = interval.getMin();
                max = interval.getMax();
                start = interval.getStartValue();
                continue;
            }
            if (data instanceof UnivariateObjectiveFunction) {
                function = ((UnivariateObjectiveFunction) data).getObjectiveFunction();
                continue;
            }
            if (data instanceof GoalType) {
                goal = (GoalType) data;
                continue;
            }
        }
    }

    /**
     * @return the initial guess.
     */
    public double getStartValue() {
        return start;
    }
    /**
     * @return the lower bounds.
     */
    public double getMin() {
        return min;
    }
    /**
     * @return the upper bounds.
     */
    public double getMax() {
        return max;
    }

    /**
     * Computes the objective function value.
     * This method <em>must</em> be called by subclasses to enforce the
     * evaluation counter limit.
     *
     * @param x Point at which the objective function must be evaluated.
     * @return the objective function value at the specified point.
     * @throws TooManyEvaluationsException if the maximal number of
     * evaluations is exceeded.
     */
    protected double computeObjectiveValue(double x) {
        super.incrementEvaluationCount();
        return function.value(x);
    }
}