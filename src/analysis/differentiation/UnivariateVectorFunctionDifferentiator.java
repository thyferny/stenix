package analysis.differentiation;

import analysis.UnivariateVectorFunction;

public interface UnivariateVectorFunctionDifferentiator {

    /** Create an implementation of a {@link UnivariateDifferentiableVectorFunction
     * differential} from a regular {@link UnivariateVectorFunction vector function}.
     * @param function function to differentiate
     * @return differential function
     */
    UnivariateDifferentiableVectorFunction differentiate(UnivariateVectorFunction function);

}
