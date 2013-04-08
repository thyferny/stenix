package distribution;

import java.util.ArrayList;
import java.util.List;

import math.util.Pair;
import random.RandomGenerator;
import random.Well19937c;
import exception.DimensionMismatchException;
import exception.MathArithmeticException;
import exception.NotPositiveException;
import exception.util.LocalizedFormats;

public class MixtureMultivariateRealDistribution<T extends MultivariateRealDistribution>
    extends AbstractMultivariateRealDistribution {
    /** Normalized weight of each mixture component. */
    private final double[] weight;
    /** Mixture components. */
    private final List<T> distribution;

    /**
     * Creates a mixture model from a list of distributions and their
     * associated weights.
     *
     * @param components List of (weight, distribution) pairs from which to sample.
     */
    public MixtureMultivariateRealDistribution(List<Pair<Double, T>> components) {
        this(new Well19937c(), components);
    }

    /**
     * Creates a mixture model from a list of distributions and their
     * associated weights.
     *
     * @param rng Random number generator.
     * @param components Distributions from which to sample.
     * @throws NotPositiveException if any of the weights is negative.
     * @throws DimensionMismatchException if not all components have the same
     * number of variables.
     */
    public MixtureMultivariateRealDistribution(RandomGenerator rng,
                                               List<Pair<Double, T>> components) {
        super(rng, components.get(0).getSecond().getDimension());

        final int numComp = components.size();
        final int dim = getDimension();
        double weightSum = 0;
        for (int i = 0; i < numComp; i++) {
            final Pair<Double, T> comp = components.get(i);
            if (comp.getSecond().getDimension() != dim) {
                throw new DimensionMismatchException(comp.getSecond().getDimension(), dim);
            }
            if (comp.getFirst() < 0) {
                throw new NotPositiveException(comp.getFirst());
            }
            weightSum += comp.getFirst();
        }

        // Check for overflow.
        if (Double.isInfinite(weightSum)) {
            throw new MathArithmeticException(LocalizedFormats.OVERFLOW);
        }

        // Store each distribution and its normalized weight.
        distribution = new ArrayList<T>();
        weight = new double[numComp];
        for (int i = 0; i < numComp; i++) {
            final Pair<Double, T> comp = components.get(i);
            weight[i] = comp.getFirst() / weightSum;
            distribution.add(comp.getSecond());
        }
    }

    /** {@inheritDoc} */
    public double density(final double[] values) {
        double p = 0;
        for (int i = 0; i < weight.length; i++) {
            p += weight[i] * distribution.get(i).density(values);
        }
        return p;
    }

    /** {@inheritDoc} */
    public double[] sample() {
        // Sampled values.
        double[] vals = null;

        // Determine which component to sample from.
        final double randomValue = random.nextDouble();
        double sum = 0;

        for (int i = 0; i < weight.length; i++) {
            sum += weight[i];
            if (randomValue <= sum) {
                // pick model i
                vals = distribution.get(i).sample();
                break;
            }
        }

        if (vals == null) {
            // This should never happen, but it ensures we won't return a null in
            // case the loop above has some floating point inequality problem on
            // the final iteration.
            vals = distribution.get(weight.length - 1).sample();
        }

        return vals;
    }

    /** {@inheritDoc} */
    public void reseedRandomGenerator(long seed) {
        // Seed needs to be propagated to underlying components
        // in order to maintain consistency between runs.
        super.reseedRandomGenerator(seed);

        for (int i = 0; i < distribution.size(); i++) {
            // Make each component's seed different in order to avoid
            // using the same sequence of random numbers.
            distribution.get(i).reseedRandomGenerator(i + 1 + seed);
        }
    }

    /**
     * Gets the distributions that make up the mixture model.
     *
     * @return the component distributions and associated weights.
     */
    public List<Pair<Double, T>> getComponents() {
        final List<Pair<Double, T>> list = new ArrayList<Pair<Double, T>>();

        for (int i = 0; i < weight.length; i++) {
            list.add(new Pair<Double, T>(weight[i], distribution.get(i)));
        }

        return list;
    }
}
