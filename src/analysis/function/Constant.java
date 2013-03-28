package analysis.function;

import analysis.differentiation.DerivativeStructure;
import analysis.differentiation.UnivariateDifferentiableFunction;


public class Constant implements UnivariateDifferentiableFunction {
    /** Constant. */
    private final double c;

    /**
     * @param c Constant.
     */
    public Constant(double c) {
        this.c = c;
    }

    /** {@inheritDoc} */
    public double value(double x) {
        return c;
    }

    /** {@inheritDoc}
     * @since 3.1
     */
    public DerivativeStructure value(final DerivativeStructure t) {
        return new DerivativeStructure(t.getFreeParameters(), t.getOrder(), c);
    }

}
