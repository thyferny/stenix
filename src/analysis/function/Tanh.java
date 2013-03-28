package analysis.function;

import math.util.FastMath;
import analysis.differentiation.DerivativeStructure;
import analysis.differentiation.UnivariateDifferentiableFunction;

public class Tanh implements UnivariateDifferentiableFunction {
    /** {@inheritDoc} */
    public double value(double x) {
        return FastMath.tanh(x);
    }

    /** {@inheritDoc}
     * @since 3.1
     */
    public DerivativeStructure value(final DerivativeStructure t) {
        return t.tanh();
    }

}
