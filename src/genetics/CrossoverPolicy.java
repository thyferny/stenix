package genetics;

import exception.MathIllegalArgumentException;

public interface CrossoverPolicy {

    /**
     * Perform a crossover operation on the given chromosomes.
     *
     * @param first the first chromosome.
     * @param second the second chromosome.
     * @return the pair of new chromosomes that resulted from the crossover.
     * @throws MathIllegalArgumentException if the given chromosomes are not compatible with this {@link CrossoverPolicy}
     */
    ChromosomePair crossover(Chromosome first, Chromosome second) throws MathIllegalArgumentException;
}
