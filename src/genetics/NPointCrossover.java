package genetics;


import java.util.ArrayList;
import java.util.List;

import random.RandomGenerator;
import exception.DimensionMismatchException;
import exception.MathIllegalArgumentException;
import exception.NotStrictlyPositiveException;
import exception.NumberIsTooLargeException;
import exception.util.LocalizedFormats;

/**
 * N-point crossover policy. For each iteration a random crossover point is
 * selected and the first part from each parent is copied to the corresponding
 * child, and the second parts are copied crosswise.
 *
 * Example (2-point crossover):
 * <pre>
 * -C- denotes a crossover point
 *           -C-       -C-                         -C-        -C-
 * p1 = (1 0  | 1 0 0 1 | 0 1 1)    X    p2 = (0 1  | 1 0 1 0  | 1 1 1)
 *      \----/ \-------/ \-----/              \----/ \--------/ \-----/
 *        ||      (*)       ||                  ||      (**)       ||
 *        VV      (**)      VV                  VV      (*)        VV
 *      /----\ /--------\ /-----\             /----\ /--------\ /-----\
 * c1 = (1 0  | 1 0 1 0  | 0 1 1)    X   c2 = (0 1  | 1 0 0 1  | 0 1 1)
 * </pre>
 *
 * This policy works only on {@link AbstractListChromosome}, and therefore it
 * is parameterized by T. Moreover, the chromosomes must have same lengths.
 *
 * @param <T> generic type of the {@link AbstractListChromosome}s for crossover
 * @since 3.1
 * @version $Id: NPointCrossover.java 1385297 2012-09-16 16:05:57Z tn $
 */
public class NPointCrossover<T> implements CrossoverPolicy {

    /** The number of crossover points. */
    private final int crossoverPoints;

    /**
     * Creates a new {@link NPointCrossover} policy using the given number of points.
     * <p>
     * <b>Note</b>: the number of crossover points must be &lt; <code>chromosome length - 1</code>.
     * This condition can only be checked at runtime, as the chromosome length is not known in advance.
     *
     * @param crossoverPoints the number of crossover points
     * @throws NotStrictlyPositiveException if the number of {@code crossoverPoints} is not strictly positive
     */
    public NPointCrossover(final int crossoverPoints) throws NotStrictlyPositiveException {
        if (crossoverPoints <= 0) {
            throw new NotStrictlyPositiveException(crossoverPoints);
        }
        this.crossoverPoints = crossoverPoints;
    }

    /**
     * Returns the number of crossover points used by this {@link CrossoverPolicy}.
     *
     * @return the number of crossover points
     */
    public int getCrossoverPoints() {
        return crossoverPoints;
    }

    /**
     * Performs a N-point crossover. N random crossover points are selected and are used
     * to divide the parent chromosomes into segments. The segments are copied in alternate
     * order from the two parents to the corresponding child chromosomes.
     *
     * Example (2-point crossover):
     * <pre>
     * -C- denotes a crossover point
     *           -C-       -C-                         -C-        -C-
     * p1 = (1 0  | 1 0 0 1 | 0 1 1)    X    p2 = (0 1  | 1 0 1 0  | 1 1 1)
     *      \----/ \-------/ \-----/              \----/ \--------/ \-----/
     *        ||      (*)       ||                  ||      (**)       ||
     *        VV      (**)      VV                  VV      (*)        VV
     *      /----\ /--------\ /-----\             /----\ /--------\ /-----\
     * c1 = (1 0  | 1 0 1 0  | 0 1 1)    X   c2 = (0 1  | 1 0 0 1  | 0 1 1)
     * </pre>
     *
     * @param first first parent (p1)
     * @param second second parent (p2)
     * @return pair of two children (c1,c2)
     * @throws MathIllegalArgumentException iff one of the chromosomes is
     *   not an instance of {@link AbstractListChromosome}
     * @throws DimensionMismatchException if the length of the two chromosomes is different
     */
    @SuppressWarnings("unchecked") // OK because of instanceof checks
    public ChromosomePair crossover(final Chromosome first, final Chromosome second)
        throws DimensionMismatchException, MathIllegalArgumentException {

        if (!(first instanceof AbstractListChromosome<?> && second instanceof AbstractListChromosome<?>)) {
            throw new MathIllegalArgumentException(LocalizedFormats.INVALID_FIXED_LENGTH_CHROMOSOME);
        }
        return mate((AbstractListChromosome<T>) first, (AbstractListChromosome<T>) second);
    }

    /**
     * Helper for {@link #crossover(Chromosome, Chromosome)}. Performs the actual crossover.
     *
     * @param first the first chromosome
     * @param second the second chromosome
     * @return the pair of new chromosomes that resulted from the crossover
     * @throws DimensionMismatchException if the length of the two chromosomes is different
     * @throws NumberIsTooLargeException if the number of crossoverPoints is too large for the actual chromosomes
     */
    private ChromosomePair mate(final AbstractListChromosome<T> first,
                                final AbstractListChromosome<T> second)
        throws DimensionMismatchException, NumberIsTooLargeException {

        final int length = first.getLength();
        if (length != second.getLength()) {
            throw new DimensionMismatchException(second.getLength(), length);
        }
        if (crossoverPoints >= length) {
            throw new NumberIsTooLargeException(crossoverPoints, length, false);
        }

        // array representations of the parents
        final List<T> parent1Rep = first.getRepresentation();
        final List<T> parent2Rep = second.getRepresentation();
        // and of the children
        final ArrayList<T> child1Rep = new ArrayList<T>(first.getLength());
        final ArrayList<T> child2Rep = new ArrayList<T>(second.getLength());

        final RandomGenerator random = GeneticAlgorithm.getRandomGenerator();

        ArrayList<T> c1 = child1Rep;
        ArrayList<T> c2 = child2Rep;

        int remainingPoints = crossoverPoints;
        int lastIndex = 0;
        for (int i = 0; i < crossoverPoints; i++, remainingPoints--) {
            // select the next crossover point at random
            final int crossoverIndex = 1 + lastIndex + random.nextInt(length - lastIndex - remainingPoints);

            // copy the current segment
            for (int j = lastIndex; j < crossoverIndex; j++) {
                c1.add(parent1Rep.get(j));
                c2.add(parent2Rep.get(j));
            }

            // swap the children for the next segment
            ArrayList<T> tmp = c1;
            c1 = c2;
            c2 = tmp;

            lastIndex = crossoverIndex;
        }

        // copy the last segment
        for (int j = lastIndex; j < length; j++) {
            c1.add(parent1Rep.get(j));
            c2.add(parent2Rep.get(j));
        }

        return new ChromosomePair(first.newFixedLengthChromosome(child1Rep),
                                  second.newFixedLengthChromosome(child2Rep));
    }
}
