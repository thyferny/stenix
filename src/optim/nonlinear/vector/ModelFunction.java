package optim.nonlinear.vector;

import optim.OptimizationData;
import analysis.MultivariateVectorFunction;

public class ModelFunction implements OptimizationData {
    /** Function to be optimized. */
    private final MultivariateVectorFunction model;

    /**
     * @param m Model function to be optimized.
     */
    public ModelFunction(MultivariateVectorFunction m) {
        model = m;
    }

    /**
     * Gets the model function to be optimized.
     *
     * @return the model function.
     */
    public MultivariateVectorFunction getModelFunction() {
        return model;
    }
}
