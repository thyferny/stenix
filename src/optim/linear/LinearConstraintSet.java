package optim.linear;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import optim.OptimizationData;

public class LinearConstraintSet implements OptimizationData {
    /** Set of constraints. */
    private final Set<LinearConstraint> linearConstraints
        = new HashSet<LinearConstraint>();

    /**
     * Creates a set containing the given constraints.
     *
     * @param constraints Constraints.
     */
    public LinearConstraintSet(LinearConstraint... constraints) {
        for (LinearConstraint c : constraints) {
            linearConstraints.add(c);
        }
    }

    /**
     * Creates a set containing the given constraints.
     *
     * @param constraints Constraints.
     */
    public LinearConstraintSet(Collection<LinearConstraint> constraints) {
        linearConstraints.addAll(constraints);
    }

    /**
     * Gets the set of linear constraints.
     *
     * @return the constraints.
     */
    public Collection<LinearConstraint> getConstraints() {
        return Collections.unmodifiableSet(linearConstraints);
    }
}
