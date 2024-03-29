package optim;

import random.RandomVectorGenerator;
import exception.MathIllegalStateException;
import exception.NotStrictlyPositiveException;

public abstract class BaseMultiStartMultivariateOptimizer<PAIR>
    extends BaseMultivariateOptimizer<PAIR> {
    /** Underlying classical optimizer. */
    private final BaseMultivariateOptimizer<PAIR> optimizer;
    /** Number of evaluations already performed for all starts. */
    private int totalEvaluations;
    /** Number of starts to go. */
    private int starts;
    /** Random generator for multi-start. */
    private RandomVectorGenerator generator;
    /** Optimization data. */
    private OptimizationData[] optimData;
    /**
     * Location in {@link #optimData} where the updated maximum
     * number of evaluations will be stored.
     */
    private int maxEvalIndex = -1;
    /**
     * Location in {@link #optimData} where the updated start value
     * will be stored.
     */
    private int initialGuessIndex = -1;

    /**
     * Create a multi-start optimizer from a single-start optimizer.
     *
     * @param optimizer Single-start optimizer to wrap.
     * @param starts Number of starts to perform. If {@code starts == 1},
     * the {@link #optimize(OptimizationData[]) optimize} will return the
     * same solution as the given {@code optimizer} would return.
     * @param generator Random vector generator to use for restarts.
     * @throws NotStrictlyPositiveException if {@code starts < 1}.
     */
    public BaseMultiStartMultivariateOptimizer(final BaseMultivariateOptimizer<PAIR> optimizer,
                                               final int starts,
                                               final RandomVectorGenerator generator) {
        super(optimizer.getConvergenceChecker());

        if (starts < 1) {
            throw new NotStrictlyPositiveException(starts);
        }

        this.optimizer = optimizer;
        this.starts = starts;
        this.generator = generator;
    }

    /** {@inheritDoc} */
    @Override
    public int getEvaluations() {
        return totalEvaluations;
    }

    /**
     * Gets all the optima found during the last call to {@code optimize}.
     * The optimizer stores all the optima found during a set of
     * restarts. The {@code optimize} method returns the best point only.
     * This method returns all the points found at the end of each starts,
     * including the best one already returned by the {@code optimize} method.
     * <br/>
     * The returned array as one element for each start as specified
     * in the constructor. It is ordered with the results from the
     * runs that did converge first, sorted from best to worst
     * objective value (i.e in ascending order if minimizing and in
     * descending order if maximizing), followed by {@code null} elements
     * corresponding to the runs that did not converge. This means all
     * elements will be {@code null} if the {@code optimize} method did throw
     * an exception.
     * This also means that if the first element is not {@code null}, it is
     * the best point found across all starts.
     * <br/>
     * The behaviour is undefined if this method is called before
     * {@code optimize}; it will likely throw {@code NullPointerException}.
     *
     * @return an array containing the optima sorted from best to worst.
     */
    public abstract PAIR[] getOptima();

    /**
     * {@inheritDoc}
     *
     * @throws MathIllegalStateException if {@code optData} does not contain an
     * instance of {@link MaxEval} or {@link InitialGuess}.
     */
    @Override
    public PAIR optimize(OptimizationData... optData) {
        // Store arguments in order to pass them to the internal optimizer.
       optimData = optData;
        // Set up base class and perform computations.
        return super.optimize(optData);
    }

    /** {@inheritDoc} */
    @Override
    protected PAIR doOptimize() {
        // Remove all instances of "MaxEval" and "InitialGuess" from the
        // array that will be passed to the internal optimizer.
        // The former is to enforce smaller numbers of allowed evaluations
        // (according to how many have been used up already), and the latter
        // to impose a different start value for each start.
        for (int i = 0; i < optimData.length; i++) {
            if (optimData[i] instanceof MaxEval) {
                optimData[i] = null;
                maxEvalIndex = i;
            }
            if (optimData[i] instanceof InitialGuess) {
                optimData[i] = null;
                initialGuessIndex = i;
                continue;
            }
        }
        if (maxEvalIndex == -1) {
            throw new MathIllegalStateException();
        }
        if (initialGuessIndex == -1) {
            throw new MathIllegalStateException();
        }

        RuntimeException lastException = null;
        totalEvaluations = 0;
        clear();

        final int maxEval = getMaxEvaluations();
        final double[] min = getLowerBound(); // XXX Should be used to enforce bounds (see below).
        final double[] max = getUpperBound(); // XXX Should be used to enforce bounds (see below).
        final double[] startPoint = getStartPoint();

        // Multi-start loop.
        for (int i = 0; i < starts; i++) {
            // CHECKSTYLE: stop IllegalCatch
            try {
                // Decrease number of allowed evaluations.
                optimData[maxEvalIndex] = new MaxEval(maxEval - totalEvaluations);
                // New start value.
                final double[] s = (i == 0) ?
                    startPoint :
                    generator.nextVector(); // XXX This does not enforce bounds!
                optimData[initialGuessIndex] = new InitialGuess(s);
                // Optimize.
                final PAIR result = optimizer.optimize(optimData);
                store(result);
            } catch (RuntimeException mue) {
                lastException = mue;
            }
            // CHECKSTYLE: resume IllegalCatch

            totalEvaluations += optimizer.getEvaluations();
        }

        final PAIR[] optima = getOptima();
        if (optima.length == 0) {
            // All runs failed.
            throw lastException; // Cannot be null if starts >= 1.
        }

        // Return the best optimum.
        return optima[0];
    }

    /**
     * Method that will be called in order to store each found optimum.
     *
     * @param optimum Result of an optimization run.
     */
    protected abstract void store(PAIR optimum);
    /**
     * Method that will called in order to clear all stored optima.
     */
    protected abstract void clear();
}
