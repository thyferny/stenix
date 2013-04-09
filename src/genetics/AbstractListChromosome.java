package genetics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class AbstractListChromosome<T> extends Chromosome {

    /** List representing the chromosome */
    private final List<T> representation;

    /**
     * Constructor.
     * @param representation inner representation of the chromosome
     * @throws InvalidRepresentationException iff the <code>representation</code> can not represent a valid chromosome
     */
    public AbstractListChromosome(final List<T> representation) throws InvalidRepresentationException {
        checkValidity(representation);
        this.representation = Collections.unmodifiableList(new ArrayList<T> (representation));
    }

    /**
     * Constructor.
     * @param representation inner representation of the chromosome
     * @throws InvalidRepresentationException iff the <code>representation</code> can not represent a valid chromosome
     */
    public AbstractListChromosome(final T[] representation) throws InvalidRepresentationException {
        this(Arrays.asList(representation));
    }

    /**
     * Asserts that <code>representation</code> can represent a valid chromosome.
     *
     * @param chromosomeRepresentation representation of the chromosome
     * @throws InvalidRepresentationException iff the <code>representation</code> can not represent a valid chromosome
     */
    protected abstract void checkValidity(List<T> chromosomeRepresentation) throws InvalidRepresentationException;

    /**
     * Returns the (immutable) inner representation of the chromosome.
     * @return the representation of the chromosome
     */
    protected List<T> getRepresentation() {
        return representation;
    }

    /**
     * Returns the length of the chromosome.
     * @return the length of the chromosome
     */
    public int getLength() {
        return getRepresentation().size();
    }

    /**
     * Creates a new instance of the same class as <code>this</code> is, with a given <code>arrayRepresentation</code>.
     * This is needed in crossover and mutation operators, where we need a new instance of the same class, but with
     * different array representation.
     * <p>
     * Usually, this method just calls a constructor of the class.
     *
     * @param chromosomeRepresentation the inner array representation of the new chromosome.
     * @return new instance extended from FixedLengthChromosome with the given arrayRepresentation
     */
    public abstract AbstractListChromosome<T> newFixedLengthChromosome(final List<T> chromosomeRepresentation);

    @Override
    public String toString() {
        return String.format("(f=%s %s)", getFitness(), getRepresentation());
    }
}
