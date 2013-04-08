package optim.nonlinear.scalar;

import optim.OptimizationData;
import analysis.MultivariateVectorFunction;

public class ObjectiveFunctionGradient implements OptimizationData {
    /** Function to be optimized. */
    private final MultivariateVectorFunction gradient;

    /**
     * @param g Gradient of the function to be optimized.
     */
    public ObjectiveFunctionGradient(MultivariateVectorFunction g) {
        gradient = g;
    }

    /**
     * Gets the gradient of the function to be optimized.
     *
     * @return the objective function gradient.
     */
    public MultivariateVectorFunction getObjectiveFunctionGradient() {
        return gradient;
    }
}
