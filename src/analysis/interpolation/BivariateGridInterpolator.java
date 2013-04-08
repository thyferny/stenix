package analysis.interpolation;

import analysis.BivariateFunction;
import exception.DimensionMismatchException;
import exception.NoDataException;

public interface BivariateGridInterpolator {
    /**
     * Compute an interpolating function for the dataset.
     *
     * @param xval All the x-coordinates of the interpolation points, sorted
     * in increasing order.
     * @param yval All the y-coordinates of the interpolation points, sorted
     * in increasing order.
     * @param fval The values of the interpolation points on all the grid knots:
     * {@code fval[i][j] = f(xval[i], yval[j])}.
     * @return a function which interpolates the dataset.
     * @throws NoDataException if any of the arrays has zero length.
     * @throws DimensionMismatchException if the array lengths are inconsistent.
     */
    BivariateFunction interpolate(double[] xval, double[] yval,
                                  double[][] fval)
        throws NoDataException,
               DimensionMismatchException;
}
