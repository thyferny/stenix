package analysis.differentiation;

import analysis.UnivariateFunction;
import exception.MathIllegalArgumentException;


public interface UnivariateDifferentiableFunction extends UnivariateFunction {

    /** Simple mathematical function.
     * <p>{@link UnivariateDifferentiableFunction} classes compute both the
     * value and the first derivative of the function.</p>
     * @param t function input value
     * @return function result
     * @exception MathIllegalArgumentException if {@code t} does not
     * fulfill functions constraints (argument out of bound, or unsupported
     * derivative order for example)
     */
    DerivativeStructure value(DerivativeStructure t) throws MathIllegalArgumentException;

}
