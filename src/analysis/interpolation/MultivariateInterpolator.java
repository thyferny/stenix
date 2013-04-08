package analysis.interpolation;

import analysis.MultivariateFunction;

public interface MultivariateInterpolator {

    /**
     * Computes an interpolating function for the data set.
     *
     * @param xval the arguments for the interpolation points.
     * {@code xval[i][0]} is the first component of interpolation point
     * {@code i}, {@code xval[i][1]} is the second component, and so on
     * until {@code xval[i][d-1]}, the last component of that interpolation
     * point (where {@code d} is thus the dimension of the space).
     * @param yval the values for the interpolation points
     * @return a function which interpolates the data set
     * @throws org.apache.commons.math3.exception.MathIllegalArgumentException
     * if the arguments violate assumptions made by the interpolation
     * algorithm.
     * @throws org.apache.commons.math3.exception.DimensionMismatchException
     * when the array dimensions are not consistent.
     * @throws org.apache.commons.math3.exception.NoDataException if an
     * array has zero-length.
     * @throws org.apache.commons.math3.exception.NullArgumentException if
     * the arguments are {@code null}.
     */
    MultivariateFunction interpolate(double[][] xval, double[] yval);
}
