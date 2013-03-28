package analysis.differentiation;

import analysis.UnivariateFunction;

public interface UnivariateFunctionDifferentiator {

    /** Create an implementation of a {@link UnivariateDifferentiableFunction
     * differential} from a regular {@link UnivariateFunction function}.
     * @param function function to differentiate
     * @return differential function
     */
    UnivariateDifferentiableFunction differentiate(UnivariateFunction function);

}
