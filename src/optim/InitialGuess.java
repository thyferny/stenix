package optim;

public class InitialGuess implements OptimizationData {
    /** Initial guess. */
    private final double[] init;

    /**
     * @param startPoint Initial guess.
     */
    public InitialGuess(double[] startPoint) {
        init = startPoint.clone();
    }

    /**
     * Gets the initial guess.
     *
     * @return the initial guess.
     */
    public double[] getInitialGuess() {
        return init.clone();
    }
}
