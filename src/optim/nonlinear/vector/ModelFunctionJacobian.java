package optim.nonlinear.vector;

import optim.OptimizationData;
import analysis.MultivariateMatrixFunction;

public class ModelFunctionJacobian implements OptimizationData {
    /** Function to be optimized. */
    private final MultivariateMatrixFunction jacobian;

    /**
     * @param j Jacobian of the model function to be optimized.
     */
    public ModelFunctionJacobian(MultivariateMatrixFunction j) {
        jacobian = j;
    }

    /**
     * Gets the Jacobian of the model function to be optimized.
     *
     * @return the model function Jacobian.
     */
    public MultivariateMatrixFunction getModelFunctionJacobian() {
        return jacobian;
    }
}
