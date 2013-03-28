package analysis.function;

import math.util.FastMath;
import analysis.differentiation.DerivativeStructure;
import analysis.differentiation.UnivariateDifferentiableFunction;


public class Power implements UnivariateDifferentiableFunction {
    /** Power. */
    private final double p;

    /**
     * @param p Power.
     */
    public Power(double p) {
        this.p = p;
    }

    /** {@inheritDoc} */
    public double value(double x) {
        return FastMath.pow(x, p);
    }

    /** {@inheritDoc}
     * @since 3.1
     */
    public DerivativeStructure value(final DerivativeStructure t) {
        return t.pow(p);
    }

}
