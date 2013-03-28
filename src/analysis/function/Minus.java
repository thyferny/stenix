package analysis.function;

import analysis.differentiation.DerivativeStructure;
import analysis.differentiation.UnivariateDifferentiableFunction;


public class Minus implements UnivariateDifferentiableFunction {
    /** {@inheritDoc} */
    public double value(double x) {
        return -x;
    }

    /** {@inheritDoc}
     * @since 3.1
     */
    public DerivativeStructure value(final DerivativeStructure t) {
        return t.negate();
    }

}
