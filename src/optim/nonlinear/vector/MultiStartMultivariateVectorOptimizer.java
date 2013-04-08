package optim.nonlinear.vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import linear.ArrayRealVector;
import linear.RealMatrix;
import linear.RealVector;
import optim.BaseMultiStartMultivariateOptimizer;
import optim.PointVectorValuePair;
import random.RandomVectorGenerator;
import exception.NotStrictlyPositiveException;
import exception.NullArgumentException;

public class MultiStartMultivariateVectorOptimizer
    extends BaseMultiStartMultivariateOptimizer<PointVectorValuePair> {
    /** Underlying optimizer. */
    private final MultivariateVectorOptimizer optimizer;
    /** Found optima. */
    private final List<PointVectorValuePair> optima = new ArrayList<PointVectorValuePair>();

    /**
     * Create a multi-start optimizer from a single-start optimizer.
     *
     * @param optimizer Single-start optimizer to wrap.
     * @param starts Number of starts to perform.
     * If {@code starts == 1}, the result will be same as if {@code optimizer}
     * is called directly.
     * @param generator Random vector generator to use for restarts.
     * @throws NullArgumentException if {@code optimizer} or {@code generator}
     * is {@code null}.
     * @throws NotStrictlyPositiveException if {@code starts < 1}.
     */
    public MultiStartMultivariateVectorOptimizer(final MultivariateVectorOptimizer optimizer,
                                                 final int starts,
                                                 final RandomVectorGenerator generator)
        throws NullArgumentException,
        NotStrictlyPositiveException {
        super(optimizer, starts, generator);
        this.optimizer = optimizer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PointVectorValuePair[] getOptima() {
        Collections.sort(optima, getPairComparator());
        return optima.toArray(new PointVectorValuePair[0]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void store(PointVectorValuePair optimum) {
        optima.add(optimum);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void clear() {
        optima.clear();
    }

    /**
     * @return a comparator for sorting the optima.
     */
    private Comparator<PointVectorValuePair> getPairComparator() {
        return new Comparator<PointVectorValuePair>() {
            private final RealVector target = new ArrayRealVector(optimizer.getTarget(), false);
            private final RealMatrix weight = optimizer.getWeight();

            public int compare(final PointVectorValuePair o1,
                               final PointVectorValuePair o2) {
                if (o1 == null) {
                    return (o2 == null) ? 0 : 1;
                } else if (o2 == null) {
                    return -1;
                }
                return Double.compare(weightedResidual(o1),
                                      weightedResidual(o2));
            }

            private double weightedResidual(final PointVectorValuePair pv) {
                final RealVector v = new ArrayRealVector(pv.getValueRef(), false);
                final RealVector r = target.subtract(v);
                return r.dotProduct(weight.operate(r));
            }
        };
    }
}
