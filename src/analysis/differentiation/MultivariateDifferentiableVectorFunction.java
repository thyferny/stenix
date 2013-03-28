package analysis.differentiation;

import analysis.MultivariateVectorFunction;
import exception.MathIllegalArgumentException;

public interface MultivariateDifferentiableVectorFunction
    extends MultivariateVectorFunction {

    /**
     * Compute the value for the function at the given point.
     * @param point point at which the function must be evaluated
     * @return function value for the given point
     * @exception MathIllegalArgumentException if {@code point} does not
     * fulfill functions constraints (wrong dimension, argument out of bound,
     * or unsupported derivative order for example)
     */
    DerivativeStructure[] value(DerivativeStructure[] point)
        throws MathIllegalArgumentException;

}
