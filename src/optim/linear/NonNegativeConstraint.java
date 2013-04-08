package optim.linear;

import optim.OptimizationData;

public class NonNegativeConstraint implements OptimizationData {
    /** Whether the variables are all positive. */
    private final boolean isRestricted;

    /**
     * @param restricted If {@code true}, all the variables must be positive.
     */
    public NonNegativeConstraint(boolean restricted) {
        isRestricted = restricted;
    }

    /**
     * Indicates whether all the variables must be restricted to non-negative
     * values.
     *
     * @return {@code true} if all the variables must be positive.
     */
    public boolean isRestrictedToNonNegative() {
        return isRestricted;
    }
}
