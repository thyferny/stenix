package optim.univariate;

import optim.OptimizationData;
import analysis.UnivariateFunction;

public class UnivariateObjectiveFunction implements OptimizationData {
    /** Function to be optimized. */
    private final UnivariateFunction function;

    /**
     * @param f Function to be optimized.
     */
    public UnivariateObjectiveFunction(UnivariateFunction f) {
        function = f;
    }

    /**
     * Gets the function to be optimized.
     *
     * @return the objective function.
     */
    public UnivariateFunction getObjectiveFunction() {
        return function;
    }
}
