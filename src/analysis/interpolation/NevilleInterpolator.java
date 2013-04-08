package analysis.interpolation;

import java.io.Serializable;

import analysis.polynomials.PolynomialFunctionLagrangeForm;
import exception.DimensionMismatchException;
import exception.NonMonotonicSequenceException;
import exception.NumberIsTooSmallException;

public class NevilleInterpolator implements UnivariateInterpolator,
    Serializable {

    /** serializable version identifier */
    static final long serialVersionUID = 3003707660147873733L;

    /**
     * Computes an interpolating function for the data set.
     *
     * @param x Interpolating points.
     * @param y Interpolating values.
     * @return a function which interpolates the data set
     * @throws DimensionMismatchException if the array lengths are different.
     * @throws NumberIsTooSmallException if the number of points is less than 2.
     * @throws NonMonotonicSequenceException if two abscissae have the same
     * value.
     */
    public PolynomialFunctionLagrangeForm interpolate(double x[], double y[])
        throws DimensionMismatchException,
               NumberIsTooSmallException,
               NonMonotonicSequenceException {
        return new PolynomialFunctionLagrangeForm(x, y);
    }
}
