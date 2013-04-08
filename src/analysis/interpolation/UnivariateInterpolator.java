package analysis.interpolation;

import analysis.UnivariateFunction;

public interface UnivariateInterpolator {
    /**
     * Compute an interpolating function for the dataset.
     *
     * @param xval Arguments for the interpolation points.
     * @param yval Values for the interpolation points.
     * @return a function which interpolates the dataset.
     * @throws org.apache.commons.math3.exception.MathIllegalArgumentException
     * if the arguments violate assumptions made by the interpolation
     * algorithm.
     */
    UnivariateFunction interpolate(double xval[], double yval[]);
}
