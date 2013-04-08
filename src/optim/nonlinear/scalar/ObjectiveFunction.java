package optim.nonlinear.scalar;

import optim.OptimizationData;
import analysis.MultivariateFunction;

public class ObjectiveFunction implements OptimizationData {
    /** Function to be optimized. */
    private final MultivariateFunction function;

    /**
     * @param f Function to be optimized.
     */
    public ObjectiveFunction(MultivariateFunction f) {
        function = f;
    }

    /**
     * Gets the function to be optimized.
     *
     * @return the objective function.
     */
    public MultivariateFunction getObjectiveFunction() {
        return function;
    }
}
