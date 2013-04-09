package genetics;

import exception.NumberIsTooLargeException;

public interface Population extends Iterable<Chromosome> {
    /**
     * Access the current population size.
     * @return the current population size.
     */
    int getPopulationSize();

    /**
     * Access the maximum population size.
     * @return the maximum population size.
     */
    int getPopulationLimit();

    /**
     * Start the population for the next generation.
     * @return the beginnings of the next generation.
     */
    Population nextGeneration();

    /**
     * Add the given chromosome to the population.
     * @param chromosome the chromosome to add.
     * @throws NumberIsTooLargeException if the population would exceed the population limit when adding
     *   this chromosome
     */
    void addChromosome(Chromosome chromosome) throws NumberIsTooLargeException;

    /**
     * Access the fittest chromosome in this population.
     * @return the fittest chromosome.
     */
    Chromosome getFittestChromosome();
}
