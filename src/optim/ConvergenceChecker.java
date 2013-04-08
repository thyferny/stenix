package optim;

public interface ConvergenceChecker<PAIR> {
    /**
     * Check if the optimization algorithm has converged.
     *
     * @param iteration Current iteration.
     * @param previous Best point in the previous iteration.
     * @param current Best point in the current iteration.
     * @return {@code true} if the algorithm is considered to have converged.
     */
    boolean converged(int iteration, PAIR previous, PAIR current);
}
