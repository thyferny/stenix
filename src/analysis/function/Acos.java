package analysis.function;

import math.util.FastMath;
import analysis.differentiation.DerivativeStructure;
import analysis.differentiation.UnivariateDifferentiableFunction;

public class Acos implements UnivariateDifferentiableFunction {
    /** {@inheritDoc} */
    public double value(double x) {
        return FastMath.acos(x);
    }

    /** {@inheritDoc}
     * @since 3.1
     */
    public DerivativeStructure value(final DerivativeStructure t) {
        return t.acos();
    }

}
