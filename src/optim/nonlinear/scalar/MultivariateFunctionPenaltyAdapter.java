package optim.nonlinear.scalar;

import math.util.FastMath;
import math.util.MathUtils;
import analysis.MultivariateFunction;
import exception.DimensionMismatchException;
import exception.NumberIsTooSmallException;

public class MultivariateFunctionPenaltyAdapter
    implements MultivariateFunction {
    /** Underlying bounded function. */
    private final MultivariateFunction bounded;
    /** Lower bounds. */
    private final double[] lower;
    /** Upper bounds. */
    private final double[] upper;
    /** Penalty offset. */
    private final double offset;
    /** Penalty scales. */
    private final double[] scale;

    /**
     * Simple constructor.
     * <p>
     * When the optimizer provided points are out of range, the value of the
     * penalty function will be used instead of the value of the underlying
     * function. In order for this penalty to be effective in rejecting this
     * point during the optimization process, the penalty function value should
     * be defined with care. This value is computed as:
     * <pre>
     *   penalty(point) = offset + &sum;<sub>i</sub>[scale[i] * &radic;|point[i]-boundary[i]|]
     * </pre>
     * where indices i correspond to all the components that violates their boundaries.
     * </p>
     * <p>
     * So when attempting a function minimization, offset should be larger than
     * the maximum expected value of the underlying function and scale components
     * should all be positive. When attempting a function maximization, offset
     * should be lesser than the minimum expected value of the underlying function
     * and scale components should all be negative.
     * minimization, and lesser than the minimum expected value of the underlying
     * function when attempting maximization.
     * </p>
     * <p>
     * These choices for the penalty function have two properties. First, all out
     * of range points will return a function value that is worse than the value
     * returned by any in range point. Second, the penalty is worse for large
     * boundaries violation than for small violations, so the optimizer has an hint
     * about the direction in which it should search for acceptable points.
     * </p>
     * @param bounded bounded function
     * @param lower lower bounds for each element of the input parameters array
     * (some elements may be set to {@code Double.NEGATIVE_INFINITY} for
     * unbounded values)
     * @param upper upper bounds for each element of the input parameters array
     * (some elements may be set to {@code Double.POSITIVE_INFINITY} for
     * unbounded values)
     * @param offset base offset of the penalty function
     * @param scale scale of the penalty function
     * @exception DimensionMismatchException if lower bounds, upper bounds and
     * scales are not consistent, either according to dimension or to bounadary
     * values
     */
    public MultivariateFunctionPenaltyAdapter(final MultivariateFunction bounded,
                                              final double[] lower, final double[] upper,
                                              final double offset, final double[] scale) {

        // safety checks
        MathUtils.checkNotNull(lower);
        MathUtils.checkNotNull(upper);
        MathUtils.checkNotNull(scale);
        if (lower.length != upper.length) {
            throw new DimensionMismatchException(lower.length, upper.length);
        }
        if (lower.length != scale.length) {
            throw new DimensionMismatchException(lower.length, scale.length);
        }
        for (int i = 0; i < lower.length; ++i) {
            // note the following test is written in such a way it also fails for NaN
            if (!(upper[i] >= lower[i])) {
                throw new NumberIsTooSmallException(upper[i], lower[i], true);
            }
        }

        this.bounded = bounded;
        this.lower   = lower.clone();
        this.upper   = upper.clone();
        this.offset  = offset;
        this.scale   = scale.clone();
    }

    /**
     * Computes the underlying function value from an unbounded point.
     * <p>
     * This method simply returns the value of the underlying function
     * if the unbounded point already fulfills the bounds, and compute
     * a replacement value using the offset and scale if bounds are
     * violated, without calling the function at all.
     * </p>
     * @param point unbounded point
     * @return either underlying function value or penalty function value
     */
    public double value(double[] point) {

        for (int i = 0; i < scale.length; ++i) {
            if ((point[i] < lower[i]) || (point[i] > upper[i])) {
                // bound violation starting at this component
                double sum = 0;
                for (int j = i; j < scale.length; ++j) {
                    final double overshoot;
                    if (point[j] < lower[j]) {
                        overshoot = scale[j] * (lower[j] - point[j]);
                    } else if (point[j] > upper[j]) {
                        overshoot = scale[j] * (point[j] - upper[j]);
                    } else {
                        overshoot = 0;
                    }
                    sum += FastMath.sqrt(overshoot);
                }
                return offset + sum;
            }
        }

        // all boundaries are fulfilled, we are in the expected
        // domain of the underlying function
        return bounded.value(point);
    }
}
