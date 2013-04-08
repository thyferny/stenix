package optim.nonlinear.vector;

import optim.OptimizationData;

public class Target implements OptimizationData {
    /** Target values (of the objective vector function). */
    private final double[] target;

    /**
     * @param observations Target values.
     */
    public Target(double[] observations) {
        target = observations.clone();
    }

    /**
     * Gets the initial guess.
     *
     * @return the initial guess.
     */
    public double[] getTarget() {
        return target.clone();
    }
}
