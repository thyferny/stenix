package genetics;

import java.util.ArrayList;
import java.util.List;

import exception.MathIllegalArgumentException;
import exception.util.LocalizedFormats;

public class BinaryMutation implements MutationPolicy {

    /**
     * Mutate the given chromosome. Randomly changes one gene.
     *
     * @param original the original chromosome.
     * @return the mutated chromosome.
     * @throws MathIllegalArgumentException if <code>original</code> is not an instance of {@link BinaryChromosome}.
     */
    public Chromosome mutate(Chromosome original) throws MathIllegalArgumentException {
        if (!(original instanceof BinaryChromosome)) {
            throw new MathIllegalArgumentException(LocalizedFormats.INVALID_BINARY_CHROMOSOME);
        }

        BinaryChromosome origChrom = (BinaryChromosome) original;
        List<Integer> newRepr = new ArrayList<Integer>(origChrom.getRepresentation());

        // randomly select a gene
        int geneIndex = GeneticAlgorithm.getRandomGenerator().nextInt(origChrom.getLength());
        // and change it
        newRepr.set(geneIndex, origChrom.getRepresentation().get(geneIndex) == 0 ? 1 : 0);

        Chromosome newChrom = origChrom.newFixedLengthChromosome(newRepr);
        return newChrom;
    }

}
