package genetics;

import java.util.ArrayList;
import java.util.List;

import exception.MathIllegalArgumentException;
import exception.util.LocalizedFormats;

public class RandomKeyMutation implements MutationPolicy {

    /**
     * {@inheritDoc}
     *
     * @throws MathIllegalArgumentException if <code>original</code> is not a {@link RandomKey} instance
     */
    public Chromosome mutate(final Chromosome original) throws MathIllegalArgumentException {
        if (!(original instanceof RandomKey<?>)) {
            throw new MathIllegalArgumentException(LocalizedFormats.RANDOMKEY_MUTATION_WRONG_CLASS,
                                                   original.getClass().getSimpleName());
        }

        RandomKey<?> originalRk = (RandomKey<?>) original;
        List<Double> repr = originalRk.getRepresentation();
        int rInd = GeneticAlgorithm.getRandomGenerator().nextInt(repr.size());

        List<Double> newRepr = new ArrayList<Double> (repr);
        newRepr.set(rInd, GeneticAlgorithm.getRandomGenerator().nextDouble());

        return originalRk.newFixedLengthChromosome(newRepr);
    }

}
