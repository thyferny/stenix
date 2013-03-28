package analysis.differentiation;

import analysis.MultivariateVectorFunction;

public class GradientFunction implements MultivariateVectorFunction {

    /** Underlying real-valued function. */
    private final MultivariateDifferentiableFunction f;

    /** Simple constructor.
     * @param f underlying real-valued function
     */
    public GradientFunction(final MultivariateDifferentiableFunction f) {
        this.f = f;
    }

    /** {@inheritDoc} */
    public double[] value(double[] point)
        throws IllegalArgumentException {

        // set up parameters
        final DerivativeStructure[] dsX = new DerivativeStructure[point.length];
        for (int i = 0; i < point.length; ++i) {
            dsX[i] = new DerivativeStructure(point.length, 1, i, point[i]);
        }

        // compute the derivatives
        final DerivativeStructure dsY = f.value(dsX);

        // extract the gradient
        final double[] y = new double[point.length];
        final int[] orders = new int[point.length];
        for (int i = 0; i < point.length; ++i) {
            orders[i] = 1;
            y[i] = dsY.getPartialDerivative(orders);
            orders[i] = 0;
        }

        return y;

    }

}
