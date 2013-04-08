package optim.nonlinear.scalar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import optim.BaseMultiStartMultivariateOptimizer;
import optim.PointValuePair;
import random.RandomVectorGenerator;
import exception.NotStrictlyPositiveException;
import exception.NullArgumentException;

public class MultiStartMultivariateOptimizer
    extends BaseMultiStartMultivariateOptimizer<PointValuePair> {
    /** Underlying optimizer. */
    private final MultivariateOptimizer optimizer;
    /** Found optima. */
    private final List<PointValuePair> optima = new ArrayList<PointValuePair>();

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
    public MultiStartMultivariateOptimizer(final MultivariateOptimizer optimizer,
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
    public PointValuePair[] getOptima() {
        Collections.sort(optima, getPairComparator());
        return optima.toArray(new PointValuePair[0]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void store(PointValuePair optimum) {
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
    private Comparator<PointValuePair> getPairComparator() {
        return new Comparator<PointValuePair>() {
            public int compare(final PointValuePair o1,
                               final PointValuePair o2) {
                if (o1 == null) {
                    return (o2 == null) ? 0 : 1;
                } else if (o2 == null) {
                    return -1;
                }
                final double v1 = o1.getValue();
                final double v2 = o2.getValue();
                return (optimizer.getGoalType() == GoalType.MINIMIZE) ?
                    Double.compare(v1, v2) : Double.compare(v2, v1);
            }
        };
    }
}
