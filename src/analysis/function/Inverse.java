package analysis.function;

import analysis.differentiation.DerivativeStructure;
import analysis.differentiation.UnivariateDifferentiableFunction;

public class Inverse implements UnivariateDifferentiableFunction {
    /** {@inheritDoc} */
    public double value(double x) {
        return 1 / x;
    }

    /** {@inheritDoc}
     * @since 3.1
     */
    public DerivativeStructure value(final DerivativeStructure t) {
        return t.reciprocal();
    }

}
