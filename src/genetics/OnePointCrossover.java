package genetics;

import java.util.ArrayList;
import java.util.List;

import exception.DimensionMismatchException;
import exception.MathIllegalArgumentException;
import exception.util.LocalizedFormats;


/**
 * One point crossover policy. A random crossover point is selected and the
 * first part from each parent is copied to the corresponding child, and the
 * second parts are copied crosswise.
 *
 * Example:
 * <pre>
 * -C- denotes a crossover point
 *                   -C-                                 -C-
 * p1 = (1 0 1 0 0 1  | 0 1 1)    X    p2 = (0 1 1 0 1 0  | 1 1 1)
 *      \------------/ \-----/              \------------/ \-----/
 *            ||         (*)                       ||        (**)
 *            VV         (**)                      VV        (*)
 *      /------------\ /-----\              /------------\ /-----\
 * c1 = (1 0 1 0 0 1  | 1 1 1)    X    c2 = (0 1 1 0 1 0  | 0 1 1)
 * </pre>
 *
 * This policy works only on {@link AbstractListChromosome}, and therefore it
 * is parameterized by T. Moreover, the chromosomes must have same lengths.
 *
 * @param <T> generic type of the {@link AbstractListChromosome}s for crossover
 * @since 2.0
 * @version $Id: OnePointCrossover.java 1416643 2012-12-03 19:37:14Z tn $
 *
 */
public class OnePointCrossover<T> implements CrossoverPolicy {

    /**
     * Performs one point crossover. A random crossover point is selected and the
     * first part from each parent is copied to the corresponding child, and the
     * second parts are copied crosswise.
     *
     * Example:
     * <pre>
     * -C- denotes a crossover point
     *                   -C-                                 -C-
     * p1 = (1 0 1 0 0 1  | 0 1 1)    X    p2 = (0 1 1 0 1 0  | 1 1 1)
     *      \------------/ \-----/              \------------/ \-----/
     *            ||         (*)                       ||        (**)
     *            VV         (**)                      VV        (*)
     *      /------------\ /-----\              /------------\ /-----\
     * c1 = (1 0 1 0 0 1  | 1 1 1)    X    c2 = (0 1 1 0 1 0  | 0 1 1)
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

        if (! (first instanceof AbstractListChromosome<?> && second instanceof AbstractListChromosome<?>)) {
            throw new MathIllegalArgumentException(LocalizedFormats.INVALID_FIXED_LENGTH_CHROMOSOME);
        }
        return crossover((AbstractListChromosome<T>) first, (AbstractListChromosome<T>) second);
    }


    /**
     * Helper for {@link #crossover(Chromosome, Chromosome)}. Performs the actual crossover.
     *
     * @param first the first chromosome.
     * @param second the second chromosome.
     * @return the pair of new chromosomes that resulted from the crossover.
     * @throws DimensionMismatchException if the length of the two chromosomes is different
     */
    private ChromosomePair crossover(final AbstractListChromosome<T> first,
                                     final AbstractListChromosome<T> second) throws DimensionMismatchException {
        final int length = first.getLength();
        if (length != second.getLength()) {
            throw new DimensionMismatchException(second.getLength(), length);
        }

        // array representations of the parents
        final List<T> parent1Rep = first.getRepresentation();
        final List<T> parent2Rep = second.getRepresentation();
        // and of the children
        final ArrayList<T> child1Rep = new ArrayList<T> (first.getLength());
        final ArrayList<T> child2Rep = new ArrayList<T> (second.getLength());

        // select a crossover point at random (0 and length makes no sense)
        final int crossoverIndex = 1 + (GeneticAlgorithm.getRandomGenerator().nextInt(length-2));

        // copy the first part
        for (int i = 0; i < crossoverIndex; i++) {
            child1Rep.add(parent1Rep.get(i));
            child2Rep.add(parent2Rep.get(i));
        }
        // and switch the second part
        for (int i = crossoverIndex; i < length; i++) {
            child1Rep.add(parent2Rep.get(i));
            child2Rep.add(parent1Rep.get(i));
        }

        return new ChromosomePair(first.newFixedLengthChromosome(child1Rep),
                                  second.newFixedLengthChromosome(child2Rep));
    }

}
