package optim.nonlinear.scalar;

import math.util.FastMath;
import math.util.MathUtils;
import analysis.MultivariateFunction;
import analysis.UnivariateFunction;
import analysis.function.Logit;
import analysis.function.Sigmoid;
import exception.DimensionMismatchException;
import exception.NumberIsTooSmallException;

public class MultivariateFunctionMappingAdapter
    implements MultivariateFunction {
    /** Underlying bounded function. */
    private final MultivariateFunction bounded;
    /** Mapping functions. */
    private final Mapper[] mappers;

    /** Simple constructor.
     * @param bounded bounded function
     * @param lower lower bounds for each element of the input parameters array
     * (some elements may be set to {@code Double.NEGATIVE_INFINITY} for
     * unbounded values)
     * @param upper upper bounds for each element of the input parameters array
     * (some elements may be set to {@code Double.POSITIVE_INFINITY} for
     * unbounded values)
     * @exception DimensionMismatchException if lower and upper bounds are not
     * consistent, either according to dimension or to values
     */
    public MultivariateFunctionMappingAdapter(final MultivariateFunction bounded,
                                              final double[] lower, final double[] upper) {
        // safety checks
        MathUtils.checkNotNull(lower);
        MathUtils.checkNotNull(upper);
        if (lower.length != upper.length) {
            throw new DimensionMismatchException(lower.length, upper.length);
        }
        for (int i = 0; i < lower.length; ++i) {
            // note the following test is written in such a way it also fails for NaN
            if (!(upper[i] >= lower[i])) {
                throw new NumberIsTooSmallException(upper[i], lower[i], true);
            }
        }

        this.bounded = bounded;
        this.mappers = new Mapper[lower.length];
        for (int i = 0; i < mappers.length; ++i) {
            if (Double.isInfinite(lower[i])) {
                if (Double.isInfinite(upper[i])) {
                    // element is unbounded, no transformation is needed
                    mappers[i] = new NoBoundsMapper();
                } else {
                    // element is simple-bounded on the upper side
                    mappers[i] = new UpperBoundMapper(upper[i]);
                }
            } else {
                if (Double.isInfinite(upper[i])) {
                    // element is simple-bounded on the lower side
                    mappers[i] = new LowerBoundMapper(lower[i]);
                } else {
                    // element is double-bounded
                    mappers[i] = new LowerUpperBoundMapper(lower[i], upper[i]);
                }
            }
        }
    }

    /**
     * Maps an array from unbounded to bounded.
     *
     * @param point Unbounded values.
     * @return the bounded values.
     */
    public double[] unboundedToBounded(double[] point) {
        // Map unbounded input point to bounded point.
        final double[] mapped = new double[mappers.length];
        for (int i = 0; i < mappers.length; ++i) {
            mapped[i] = mappers[i].unboundedToBounded(point[i]);
        }

        return mapped;
    }

    /**
     * Maps an array from bounded to unbounded.
     *
     * @param point Bounded values.
     * @return the unbounded values.
     */
    public double[] boundedToUnbounded(double[] point) {
        // Map bounded input point to unbounded point.
        final double[] mapped = new double[mappers.length];
        for (int i = 0; i < mappers.length; ++i) {
            mapped[i] = mappers[i].boundedToUnbounded(point[i]);
        }

        return mapped;
    }

    /**
     * Compute the underlying function value from an unbounded point.
     * <p>
     * This method simply bounds the unbounded point using the mappings
     * set up at construction and calls the underlying function using
     * the bounded point.
     * </p>
     * @param point unbounded value
     * @return underlying function value
     * @see #unboundedToBounded(double[])
     */
    public double value(double[] point) {
        return bounded.value(unboundedToBounded(point));
    }

    /** Mapping interface. */
    private interface Mapper {
        /**
         * Maps a value from unbounded to bounded.
         *
         * @param y Unbounded value.
         * @return the bounded value.
         */
        double unboundedToBounded(double y);

        /**
         * Maps a value from bounded to unbounded.
         *
         * @param x Bounded value.
         * @return the unbounded value.
         */
        double boundedToUnbounded(double x);
    }

    /** Local class for no bounds mapping. */
    private static class NoBoundsMapper implements Mapper {
        /** {@inheritDoc} */
        public double unboundedToBounded(final double y) {
            return y;
        }

        /** {@inheritDoc} */
        public double boundedToUnbounded(final double x) {
            return x;
        }
    }

    /** Local class for lower bounds mapping. */
    private static class LowerBoundMapper implements Mapper {
        /** Low bound. */
        private final double lower;

        /**
         * Simple constructor.
         *
         * @param lower lower bound
         */
        public LowerBoundMapper(final double lower) {
            this.lower = lower;
        }

        /** {@inheritDoc} */
        public double unboundedToBounded(final double y) {
            return lower + FastMath.exp(y);
        }

        /** {@inheritDoc} */
        public double boundedToUnbounded(final double x) {
            return FastMath.log(x - lower);
        }

    }

    /** Local class for upper bounds mapping. */
    private static class UpperBoundMapper implements Mapper {

        /** Upper bound. */
        private final double upper;

        /** Simple constructor.
         * @param upper upper bound
         */
        public UpperBoundMapper(final double upper) {
            this.upper = upper;
        }

        /** {@inheritDoc} */
        public double unboundedToBounded(final double y) {
            return upper - FastMath.exp(-y);
        }

        /** {@inheritDoc} */
        public double boundedToUnbounded(final double x) {
            return -FastMath.log(upper - x);
        }

    }

    /** Local class for lower and bounds mapping. */
    private static class LowerUpperBoundMapper implements Mapper {
        /** Function from unbounded to bounded. */
        private final UnivariateFunction boundingFunction;
        /** Function from bounded to unbounded. */
        private final UnivariateFunction unboundingFunction;

        /**
         * Simple constructor.
         *
         * @param lower lower bound
         * @param upper upper bound
         */
        public LowerUpperBoundMapper(final double lower, final double upper) {
            boundingFunction   = new Sigmoid(lower, upper);
            unboundingFunction = new Logit(lower, upper);
        }

        /** {@inheritDoc} */
        public double unboundedToBounded(final double y) {
            return boundingFunction.value(y);
        }

        /** {@inheritDoc} */
        public double boundedToUnbounded(final double x) {
            return unboundingFunction.value(x);
        }
    }
}
