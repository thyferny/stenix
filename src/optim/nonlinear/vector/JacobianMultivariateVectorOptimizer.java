package optim.nonlinear.vector;

import optim.ConvergenceChecker;
import optim.OptimizationData;
import optim.PointVectorValuePair;
import analysis.MultivariateMatrixFunction;
import exception.DimensionMismatchException;
import exception.TooManyEvaluationsException;


public abstract class JacobianMultivariateVectorOptimizer
    extends MultivariateVectorOptimizer {
    /**
     * Jacobian of the model function.
     */
    private MultivariateMatrixFunction jacobian;

    /**
     * @param checker Convergence checker.
     */
    protected JacobianMultivariateVectorOptimizer(ConvergenceChecker<PointVectorValuePair> checker) {
        super(checker);
    }

    /**
     * Computes the Jacobian matrix.
     *
     * @param params Point at which the Jacobian must be evaluated.
     * @return the Jacobian at the specified point.
     */
    protected double[][] computeJacobian(final double[] params) {
        return jacobian.value(params);
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
     *  <li>{@link ModelFunctionJacobian}</li>
     * </ul>
     * @return {@inheritDoc}
     * @throws TooManyEvaluationsException if the maximal number of
     * evaluations is exceeded.
     * @throws DimensionMismatchException if the initial guess, target, and weight
     * arguments have inconsistent dimensions.
     */
    @Override
    public PointVectorValuePair optimize(OptimizationData... optData)
        throws TooManyEvaluationsException,
               DimensionMismatchException {
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
     *  <li>{@link ModelFunctionJacobian}</li>
     * </ul>
     */
    private void parseOptimizationData(OptimizationData... optData) {
        // The existing values (as set by the previous call) are reused if
        // not provided in the argument list.
        for (OptimizationData data : optData) {
            if (data instanceof ModelFunctionJacobian) {
                jacobian = ((ModelFunctionJacobian) data).getModelFunctionJacobian();
                // If more data must be parsed, this statement _must_ be
                // changed to "continue".
                break;
            }
        }
    }
}
