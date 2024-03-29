package analysis.function;

import java.util.Arrays;

import math.util.MathArrays;
import analysis.UnivariateFunction;
import exception.DimensionMismatchException;
import exception.NoDataException;
import exception.NullArgumentException;

public class StepFunction implements UnivariateFunction {
    /** Abscissae. */
    private final double[] abscissa;
    /** Ordinates. */
    private final double[] ordinate;

    /**
     * Builds a step function from a list of arguments and the corresponding
     * values. Specifically, returns the function h(x) defined by <pre><code>
     * h(x) = y[0] for all x < x[1]
     *        y[1] for x[1] <= x < x[2]
     *        ...
     *        y[y.length - 1] for x >= x[x.length - 1]
     * </code></pre>
     * The value of {@code x[0]} is ignored, but it must be strictly less than
     * {@code x[1]}.
     *
     * @param x Domain values where the function changes value.
     * @param y Values of the function.
     * @throws org.apache.commons.math3.exception.NonMonotonicSequenceException
     * if the {@code x} array is not sorted in strictly increasing order.
     * @throws NullArgumentException if {@code x} or {@code y} are {@code null}.
     * @throws NoDataException if {@code x} or {@code y} are zero-length.
     * @throws DimensionMismatchException if {@code x} and {@code y} do not
     * have the same length.
     */
    public StepFunction(double[] x,
                        double[] y)
        throws NullArgumentException,
               NoDataException,
               DimensionMismatchException {
        if (x == null ||
            y == null) {
            throw new NullArgumentException();
        }
        if (x.length == 0 ||
            y.length == 0) {
            throw new NoDataException();
        }
        if (y.length != x.length) {
            throw new DimensionMismatchException(y.length, x.length);
        }
        MathArrays.checkOrder(x);

        abscissa = MathArrays.copyOf(x);
        ordinate = MathArrays.copyOf(y);
    }

    /** {@inheritDoc} */
    public double value(double x) {
        int index = Arrays.binarySearch(abscissa, x);
        double fx = 0;

        if (index < -1) {
            // "x" is between "abscissa[-index-2]" and "abscissa[-index-1]".
            fx = ordinate[-index-2];
        } else if (index >= 0) {
            // "x" is exactly "abscissa[index]".
            fx = ordinate[index];
        } else {
            // Otherwise, "x" is smaller than the first value in "abscissa"
            // (hence the returned value should be "ordinate[0]").
            fx = ordinate[0];
        }

        return fx;
    }
}
