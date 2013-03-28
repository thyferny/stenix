package analysis.differentiation;

import analysis.UnivariateMatrixFunction;
import exception.MathIllegalArgumentException;

public interface UnivariateDifferentiableMatrixFunction
    extends UnivariateMatrixFunction {

    /**
     * Compute the value for the function.
     * @param x the point for which the function value should be computed
     * @return the value
     * @exception MathIllegalArgumentException if {@code x} does not
     * fulfill functions constraints (argument out of bound, or unsupported
     * derivative order for example)
     */
    DerivativeStructure[][] value(DerivativeStructure x) throws MathIllegalArgumentException;

}
