package analysis.function;

import math.util.FastMath;
import analysis.differentiation.DerivativeStructure;
import analysis.differentiation.UnivariateDifferentiableFunction;


public class Atanh implements UnivariateDifferentiableFunction {
    /** {@inheritDoc} */
    public double value(double x) {
        return FastMath.atanh(x);
    }

    /** {@inheritDoc}
     * @since 3.1
     */
    public DerivativeStructure value(final DerivativeStructure t) {
        return t.atanh();
    }

}
