package optim.nonlinear.vector;

import linear.RealMatrix;
import optim.BaseMultivariateOptimizer;
import optim.ConvergenceChecker;
import optim.OptimizationData;
import optim.PointVectorValuePair;
import analysis.MultivariateVectorFunction;
import exception.DimensionMismatchException;
import exception.TooManyEvaluationsException;

public abstract class MultivariateVectorOptimizer
    extends BaseMultivariateOptimizer<PointVectorValuePair> {
    /** Target values for the model function at optimum. */
    private double[] target;
    /** Weight matrix. */
    private RealMatrix weightMatrix;
    /** Model function. */
    private MultivariateVectorFunction model;

    /**
     * @param checker Convergence checker.
     */
    protected MultivariateVectorOptimizer(ConvergenceChecker<PointVectorValuePair> checker) {
        super(checker);
    }

    /**
     * Computes the objective function value.
     * This method <em>must</em> be called by subclasses to enforce the
     * evaluation counter limit.
     *
     * @param params Point at which the objective function must be evaluated.
     * @return the objective function value at the specified point.
     * @throws TooManyEvaluationsException if the maximal number of evaluations
     * (of the model vector function) is exceeded.
     */
    protected double[] computeObjectiveValue(double[] params) {
        super.incrementEvaluationCount();
        return model.value(params);
    }

    /**
     * {@inheritDoc}
     *
     * @param optData Optimization data. The following data will be looked for:
     * <ul>
     *  <li>{@link org.apache.commons.math3.optim.MaxEval}</li>
     *  <li>{@link org.apache.commons.math3.optim.InitialGuess}</li>
     *  <li>{@link org.apache.commons.math3.optim.SimpleBounds}</li>
     *  <li>{@link Target}</li>
     *  <li>{@link Weight}</li>
     *  <li>{@link ModelFunction}</li>
     * </ul>
     * @return {@inheritDoc}
     * @throws TooManyEvaluationsException if the maximal number of
     * evaluations is exceeded.
     * @throws DimensionMismatchException if the initial guess, target, and weight
     * arguments have inconsistent dimensions.
     */
    public PointVectorValuePair optimize(OptimizationData... optData)
        throws TooManyEvaluationsException,
               DimensionMismatchException {
        // Retrieve settings.
        parseOptimizationData(optData);
        // Check input consistency.
        checkParameters();
        // Set up base class and perform computation.
        return super.optimize(optData);
    }

    /**
     * Gets the weight matrix of the observations.
     *
     * @return the weight matrix.
     */
    public RealMatrix getWeight() {
        return weightMatrix.copy();
    }
    /**
     * Gets the observed values to be matched by the objective vector
     * function.
     *
     * @return the target values.
     */
    public double[] getTarget() {
        return target.clone();
    }

    /**
     * Gets the number of observed values.
     *
     * @return the length of the target vector.
     */
    public int getTargetSize() {
        return target.length;
    }

    /**
     * Scans the list of (required and optional) optimization data that
     * characterize the problem.
     *
     * @param optData Optimization data. The following data will be looked for:
     * <ul>
     *  <li>{@link Target}</li>
     *  <li>{@link Weight}</li>
     *  <li>{@link ModelFunction}</li>
     * </ul>
     */
    private void parseOptimizationData(OptimizationData... optData) {
        // The existing values (as set by the previous call) are reused if
        // not provided in the argument list.
        for (OptimizationData data : optData) {
            if (data instanceof ModelFunction) {
                model = ((ModelFunction) data).getModelFunction();
                continue;
            }
            if (data instanceof Target) {
                target = ((Target) data).getTarget();
                continue;
            }
            if (data instanceof Weight) {
                weightMatrix = ((Weight) data).getWeight();
                continue;
            }
        }
    }

    /**
     * Check parameters consistency.
     *
     * @throws DimensionMismatchException if {@link #target} and
     * {@link #weightMatrix} have inconsistent dimensions.
     */
    private void checkParameters() {
        if (target.length != weightMatrix.getColumnDimension()) {
            throw new DimensionMismatchException(target.length,
                                                 weightMatrix.getColumnDimension());
        }
    }
}
