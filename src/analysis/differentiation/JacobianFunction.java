package analysis.differentiation;

import analysis.MultivariateMatrixFunction;

public class JacobianFunction implements MultivariateMatrixFunction {

    /** Underlying vector-valued function. */
    private final MultivariateDifferentiableVectorFunction f;

    /** Simple constructor.
     * @param f underlying vector-valued function
     */
    public JacobianFunction(final MultivariateDifferentiableVectorFunction f) {
        this.f = f;
    }

    /** {@inheritDoc} */
    public double[][] value(double[] point)
        throws IllegalArgumentException {

        // set up parameters
        final DerivativeStructure[] dsX = new DerivativeStructure[point.length];
        for (int i = 0; i < point.length; ++i) {
            dsX[i] = new DerivativeStructure(point.length, 1, i, point[i]);
        }

        // compute the derivatives
        final DerivativeStructure[] dsY = f.value(dsX);

        // extract the Jacobian
        final double[][] y = new double[dsY.length][point.length];
        final int[] orders = new int[point.length];
        for (int i = 0; i < dsY.length; ++i) {
            for (int j = 0; j < point.length; ++j) {
                orders[j] = 1;
                y[i][j] = dsY[i].getPartialDerivative(orders);
                orders[j] = 0;
            }
        }

        return y;

    }

}
