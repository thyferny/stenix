package analysis.differentiation;

import analysis.UnivariateMatrixFunction;

public interface UnivariateMatrixFunctionDifferentiator {

    /** Create an implementation of a {@link UnivariateDifferentiableMatrixFunction
     * differential} from a regular {@link UnivariateMatrixFunction matrix function}.
     * @param function function to differentiate
     * @return differential function
     */
    UnivariateDifferentiableMatrixFunction differentiate(UnivariateMatrixFunction function);

}
