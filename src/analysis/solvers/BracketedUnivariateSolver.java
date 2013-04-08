package analysis.solvers;

import analysis.UnivariateFunction;

public interface BracketedUnivariateSolver<FUNC extends UnivariateFunction>
    extends BaseUnivariateSolver<FUNC> {

    /**
     * Solve for a zero in the given interval.
     * A solver may require that the interval brackets a single zero root.
     * Solvers that do require bracketing should be able to handle the case
     * where one of the endpoints is itself a root.
     *
     * @param maxEval Maximum number of evaluations.
     * @param f Function to solve.
     * @param min Lower bound for the interval.
     * @param max Upper bound for the interval.
     * @param allowedSolution The kind of solutions that the root-finding algorithm may
     * accept as solutions.
     * @return A value where the function is zero.
     * @throws org.apache.commons.math3.exception.MathIllegalArgumentException
     * if the arguments do not satisfy the requirements specified by the solver.
     * @throws org.apache.commons.math3.exception.TooManyEvaluationsException if
     * the allowed number of evaluations is exceeded.
     */
    double solve(int maxEval, FUNC f, double min, double max,
                 AllowedSolution allowedSolution);

    /**
     * Solve for a zero in the given interval, start at {@code startValue}.
     * A solver may require that the interval brackets a single zero root.
     * Solvers that do require bracketing should be able to handle the case
     * where one of the endpoints is itself a root.
     *
     * @param maxEval Maximum number of evaluations.
     * @param f Function to solve.
     * @param min Lower bound for the interval.
     * @param max Upper bound for the interval.
     * @param startValue Start value to use.
     * @param allowedSolution The kind of solutions that the root-finding algorithm may
     * accept as solutions.
     * @return A value where the function is zero.
     * @throws org.apache.commons.math3.exception.MathIllegalArgumentException
     * if the arguments do not satisfy the requirements specified by the solver.
     * @throws org.apache.commons.math3.exception.TooManyEvaluationsException if
     * the allowed number of evaluations is exceeded.
     */
    double solve(int maxEval, FUNC f, double min, double max, double startValue,
                 AllowedSolution allowedSolution);

}
