package analysis.interpolation;

import analysis.TrivariateFunction;
import exception.DimensionMismatchException;
import exception.NoDataException;

public interface TrivariateGridInterpolator {
    /**
     * Compute an interpolating function for the dataset.
     *
     * @param xval All the x-coordinates of the interpolation points, sorted
     * in increasing order.
     * @param yval All the y-coordinates of the interpolation points, sorted
     * in increasing order.
     * @param zval All the z-coordinates of the interpolation points, sorted
     * in increasing order.
     * @param fval the values of the interpolation points on all the grid knots:
     * {@code fval[i][j][k] = f(xval[i], yval[j], zval[k])}.
     * @return a function that interpolates the data set.
     * @throws NoDataException if any of the arrays has zero length.
     * @throws DimensionMismatchException if the array lengths are inconsistent.
     */
    TrivariateFunction interpolate(double[] xval, double[] yval, double[] zval,
                                   double[][][] fval)
        throws NoDataException,
               DimensionMismatchException;
}
