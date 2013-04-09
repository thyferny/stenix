package genetics;

public interface StoppingCondition {
    /**
     * Determine whether or not the given population satisfies the stopping condition.
     *
     * @param population the population to test.
     * @return <code>true</code> if this stopping condition is met by the given population,
     *   <code>false</code> otherwise.
     */
    boolean isSatisfied(Population population);
}
