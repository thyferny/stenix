package analysis.solvers;

import analysis.differentiation.DerivativeStructure;
import analysis.differentiation.UnivariateDifferentiableFunction;

public abstract class AbstractUnivariateDifferentiableSolver
    extends BaseAbstractUnivariateSolver<UnivariateDifferentiableFunction>
    implements UnivariateDifferentiableSolver {

    /** Function to solve. */
    private UnivariateDifferentiableFunction function;

    /**
     * Construct a solver with given absolute accuracy.
     *
     * @param absoluteAccuracy Maximum absolute error.
     */
    protected AbstractUnivariateDifferentiableSolver(final double absoluteAccuracy) {
        super(absoluteAccuracy);
    }

    /**
     * Construct a solver with given accuracies.
     *
     * @param relativeAccuracy Maximum relative error.
     * @param absoluteAccuracy Maximum absolute error.
     * @param functionValueAccuracy Maximum function value error.
     */
    protected AbstractUnivariateDifferentiableSolver(final double relativeAccuracy,
                                                     final double absoluteAccuracy,
                                                     final double functionValueAccuracy) {
        super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
    }

    /**
     * Compute the objective function value.
     *
     * @param point Point at which the objective function must be evaluated.
     * @return the objective function value and derivative at specified point.
     * @throws org.apache.commons.math3.exception.TooManyEvaluationsException
     * if the maximal number of evaluations is exceeded.
     */
    protected DerivativeStructure computeObjectiveValueAndDerivative(double point) {
        incrementEvaluationCount();
        return function.value(new DerivativeStructure(1, 1, 0, point));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void setup(int maxEval, UnivariateDifferentiableFunction f,
                         double min, double max, double startValue) {
        super.setup(maxEval, f, min, max, startValue);
        function = f;
    }
}
