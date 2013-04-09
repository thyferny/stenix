package genetics;

import java.util.List;

public interface PermutationChromosome<T> {

    /**
     * Permutes the <code>sequence</code> of objects of type T according to the
     * permutation this chromosome represents. For example, if this chromosome
     * represents a permutation (3,0,1,2), and the unpermuted sequence is
     * (a,b,c,d), this yields (d,a,b,c).
     *
     * @param sequence the unpermuted (original) sequence of objects
     * @return permutation of <code>sequence</code> represented by this permutation
     */
    List<T> decode(List<T> sequence);

}
