package optim.linear;

import java.util.Collection;
import java.util.Collections;

import optim.OptimizationData;
import optim.PointValuePair;
import optim.nonlinear.scalar.MultivariateOptimizer;
import exception.TooManyIterationsException;

public abstract class LinearOptimizer
    extends MultivariateOptimizer {
    /**
     * Linear objective function.
     */
    private LinearObjectiveFunction function;
    /**
     * Linear constraints.
     */
    private Collection<LinearConstraint> linearConstraints;
    /**
     * Whether to restrict the variables to non-negative values.
     */
    private boolean nonNegative;

    /**
     * Simple constructor with default settings.
     *
     */
    protected LinearOptimizer() {
        super(null); // No convergence checker.
    }

    /**
     * @return {@code true} if the variables are restricted to non-negative values.
     */
    protected boolean isRestrictedToNonNegative() {
        return nonNegative;
    }

    /**
     * @return the optimization type.
     */
    protected LinearObjectiveFunction getFunction() {
        return function;
    }

    /**
     * @return the optimization type.
     */
    protected Collection<LinearConstraint> getConstraints() {
        return Collections.unmodifiableCollection(linearConstraints);
    }

    /**
     * {@inheritDoc}
     *
     * @param optData Optimization data. The following data will be looked for:
     * <ul>
     *  <li>{@link org.apache.commons.math3.optim.MaxIter}</li>
     *  <li>{@link LinearObjectiveFunction}</li>
     *  <li>{@link LinearConstraintSet}</li>
     *  <li>{@link NonNegativeConstraint}</li>
     * </ul>
     * @return {@inheritDoc}
     * @throws TooManyIterationsException if the maximal number of
     * iterations is exceeded.
     */
    @Override
    public PointValuePair optimize(OptimizationData... optData)
        throws TooManyIterationsException {
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
     *  <li>{@link LinearObjectiveFunction}</li>
     *  <li>{@link LinearConstraintSet}</li>
     *  <li>{@link NonNegativeConstraint}</li>
     * </ul>
     */
    private void parseOptimizationData(OptimizationData... optData) {
        // The existing values (as set by the previous call) are reused if
        // not provided in the argument list.
        for (OptimizationData data : optData) {
            if (data instanceof LinearObjectiveFunction) {
                function = (LinearObjectiveFunction) data;
                continue;
            }
            if (data instanceof LinearConstraintSet) {
                linearConstraints = ((LinearConstraintSet) data).getConstraints();
                continue;
            }
            if  (data instanceof NonNegativeConstraint) {
                nonNegative = ((NonNegativeConstraint) data).isRestrictedToNonNegative();
                continue;
            }
        }
    }
}
