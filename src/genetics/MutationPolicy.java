package genetics;

import exception.MathIllegalArgumentException;

public interface MutationPolicy {

    /**
     * Mutate the given chromosome.
     * @param original the original chromosome.
     * @return the mutated chromosome.
     * @throws MathIllegalArgumentException if the given chromosome is not compatible with this {@link MutationPolicy}
     */
    Chromosome mutate(Chromosome original) throws MathIllegalArgumentException;
}
