package genetics;

import java.util.Collections;
import java.util.List;

import math.util.FastMath;
import exception.NotPositiveException;
import exception.NullArgumentException;
import exception.NumberIsTooLargeException;
import exception.OutOfRangeException;
import exception.util.LocalizedFormats;

public class ElitisticListPopulation extends ListPopulation {

    /** percentage of chromosomes copied to the next generation */
    private double elitismRate = 0.9;

    /**
     * Creates a new {@link ElitisticListPopulation} instance.
     *
     * @param chromosomes list of chromosomes in the population
     * @param populationLimit maximal size of the population
     * @param elitismRate how many best chromosomes will be directly transferred to the next generation [in %]
     * @throws NullArgumentException if the list of chromosomes is {@code null}
     * @throws NotPositiveException if the population limit is not a positive number (&lt; 1)
     * @throws NumberIsTooLargeException if the list of chromosomes exceeds the population limit
     * @throws OutOfRangeException if the elitism rate is outside the [0, 1] range
     */
    public ElitisticListPopulation(final List<Chromosome> chromosomes, final int populationLimit,
                                   final double elitismRate)
        throws NullArgumentException, NotPositiveException, NumberIsTooLargeException, OutOfRangeException {

        super(chromosomes, populationLimit);
        setElitismRate(elitismRate);

    }

    /**
     * Creates a new {@link ElitisticListPopulation} instance and initializes its inner chromosome list.
     *
     * @param populationLimit maximal size of the population
     * @param elitismRate how many best chromosomes will be directly transferred to the next generation [in %]
     * @throws NotPositiveException if the population limit is not a positive number (&lt; 1)
     * @throws OutOfRangeException if the elitism rate is outside the [0, 1] range
     */
    public ElitisticListPopulation(final int populationLimit, final double elitismRate)
        throws NotPositiveException, OutOfRangeException {

        super(populationLimit);
        setElitismRate(elitismRate);

    }

    /**
     * Start the population for the next generation. The <code>{@link #elitismRate}</code>
     * percents of the best chromosomes are directly copied to the next generation.
     *
     * @return the beginnings of the next generation.
     */
    public Population nextGeneration() {
        // initialize a new generation with the same parameters
        ElitisticListPopulation nextGeneration =
                new ElitisticListPopulation(getPopulationLimit(), getElitismRate());

        final List<Chromosome> oldChromosomes = getChromosomeList();
        Collections.sort(oldChromosomes);

        // index of the last "not good enough" chromosome
        int boundIndex = (int) FastMath.ceil((1.0 - getElitismRate()) * oldChromosomes.size());
        for (int i = boundIndex; i < oldChromosomes.size(); i++) {
            nextGeneration.addChromosome(oldChromosomes.get(i));
        }
        return nextGeneration;
    }

    /**
     * Sets the elitism rate, i.e. how many best chromosomes will be directly transferred to the next generation [in %].
     *
     * @param elitismRate how many best chromosomes will be directly transferred to the next generation [in %]
     * @throws OutOfRangeException if the elitism rate is outside the [0, 1] range
     */
    public void setElitismRate(final double elitismRate) throws OutOfRangeException {
        if (elitismRate < 0 || elitismRate > 1) {
            throw new OutOfRangeException(LocalizedFormats.ELITISM_RATE, elitismRate, 0, 1);
        }
        this.elitismRate = elitismRate;
    }

    /**
     * Access the elitism rate.
     * @return the elitism rate
     */
    public double getElitismRate() {
        return this.elitismRate;
    }

}
