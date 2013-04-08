package analysis.interpolation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import math.util.ArithmeticUtils;
import analysis.differentiation.DerivativeStructure;
import analysis.differentiation.UnivariateDifferentiableVectorFunction;
import analysis.polynomials.PolynomialFunction;
import exception.MathArithmeticException;
import exception.NoDataException;
import exception.ZeroException;
import exception.util.LocalizedFormats;

public class HermiteInterpolator implements UnivariateDifferentiableVectorFunction {

    /** Sample abscissae. */
    private final List<Double> abscissae;

    /** Top diagonal of the divided differences array. */
    private final List<double[]> topDiagonal;

    /** Bottom diagonal of the divided differences array. */
    private final List<double[]> bottomDiagonal;

    /** Create an empty interpolator.
     */
    public HermiteInterpolator() {
        this.abscissae      = new ArrayList<Double>();
        this.topDiagonal    = new ArrayList<double[]>();
        this.bottomDiagonal = new ArrayList<double[]>();
    }

    /** Add a sample point.
     * <p>
     * This method must be called once for each sample point. It is allowed to
     * mix some calls with values only with calls with values and first
     * derivatives.
     * </p>
     * <p>
     * The point abscissae for all calls <em>must</em> be different.
     * </p>
     * @param x abscissa of the sample point
     * @param value value and derivatives of the sample point
     * (if only one row is passed, it is the value, if two rows are
     * passed the first one is the value and the second the derivative
     * and so on)
     * @exception ZeroException if the abscissa difference between added point
     * and a previous point is zero (i.e. the two points are at same abscissa)
     * @exception MathArithmeticException if the number of derivatives is larger
     * than 20, which prevents computation of a factorial
     */
    public void addSamplePoint(final double x, final double[] ... value)
        throws ZeroException, MathArithmeticException {

        for (int i = 0; i < value.length; ++i) {

            final double[] y = value[i].clone();
            if (i > 1) {
                double inv = 1.0 / ArithmeticUtils.factorial(i);
                for (int j = 0; j < y.length; ++j) {
                    y[j] *= inv;
                }
            }

            // update the bottom diagonal of the divided differences array
            final int n = abscissae.size();
            bottomDiagonal.add(n - i, y);
            double[] bottom0 = y;
            for (int j = i; j < n; ++j) {
                final double[] bottom1 = bottomDiagonal.get(n - (j + 1));
                final double inv = 1.0 / (x - abscissae.get(n - (j + 1)));
                if (Double.isInfinite(inv)) {
                    throw new ZeroException(LocalizedFormats.DUPLICATED_ABSCISSA_DIVISION_BY_ZERO, x);
                }
                for (int k = 0; k < y.length; ++k) {
                    bottom1[k] = inv * (bottom0[k] - bottom1[k]);
                }
                bottom0 = bottom1;
            }

            // update the top diagonal of the divided differences array
            topDiagonal.add(bottom0.clone());

            // update the abscissae array
            abscissae.add(x);

        }

    }

    /** Compute the interpolation polynomials.
     * @return interpolation polynomials array
     * @exception NoDataException if sample is empty
     */
    public PolynomialFunction[] getPolynomials()
        throws NoDataException {

        // safety check
        checkInterpolation();

        // iteration initialization
        final PolynomialFunction zero = polynomial(0);
        PolynomialFunction[] polynomials = new PolynomialFunction[topDiagonal.get(0).length];
        for (int i = 0; i < polynomials.length; ++i) {
            polynomials[i] = zero;
        }
        PolynomialFunction coeff = polynomial(1);

        // build the polynomials by iterating on the top diagonal of the divided differences array
        for (int i = 0; i < topDiagonal.size(); ++i) {
            double[] tdi = topDiagonal.get(i);
            for (int k = 0; k < polynomials.length; ++k) {
                polynomials[k] = polynomials[k].add(coeff.multiply(polynomial(tdi[k])));
            }
            coeff = coeff.multiply(polynomial(-abscissae.get(i), 1.0));
        }

        return polynomials;

    }

    /** Interpolate value at a specified abscissa.
     * <p>
     * Calling this method is equivalent to call the {@link PolynomialFunction#value(double)
     * value} methods of all polynomials returned by {@link #getPolynomials() getPolynomials},
     * except it does not build the intermediate polynomials, so this method is faster and
     * numerically more stable.
     * </p>
     * @param x interpolation abscissa
     * @return interpolated value
     * @exception NoDataException if sample is empty
     */
    public double[] value(double x)
        throws NoDataException {

        // safety check
        checkInterpolation();

        final double[] value = new double[topDiagonal.get(0).length];
        double valueCoeff = 1;
        for (int i = 0; i < topDiagonal.size(); ++i) {
            double[] dividedDifference = topDiagonal.get(i);
            for (int k = 0; k < value.length; ++k) {
                value[k] += dividedDifference[k] * valueCoeff;
            }
            final double deltaX = x - abscissae.get(i);
            valueCoeff *= deltaX;
        }

        return value;

    }

    /** Interpolate value at a specified abscissa.
     * <p>
     * Calling this method is equivalent to call the {@link
     * PolynomialFunction#value(DerivativeStructure) value} methods of all polynomials
     * returned by {@link #getPolynomials() getPolynomials}, except it does not build the
     * intermediate polynomials, so this method is faster and numerically more stable.
     * </p>
     * @param x interpolation abscissa
     * @return interpolated value
     * @exception NoDataException if sample is empty
     */
    public DerivativeStructure[] value(final DerivativeStructure x)
        throws NoDataException {

        // safety check
        checkInterpolation();

        final DerivativeStructure[] value = new DerivativeStructure[topDiagonal.get(0).length];
        Arrays.fill(value, x.getField().getZero());
        DerivativeStructure valueCoeff = x.getField().getOne();
        for (int i = 0; i < topDiagonal.size(); ++i) {
            double[] dividedDifference = topDiagonal.get(i);
            for (int k = 0; k < value.length; ++k) {
                value[k] = value[k].add(valueCoeff.multiply(dividedDifference[k]));
            }
            final DerivativeStructure deltaX = x.subtract(abscissae.get(i));
            valueCoeff = valueCoeff.multiply(deltaX);
        }

        return value;

    }

    /** Check interpolation can be performed.
     * @exception NoDataException if interpolation cannot be performed
     * because sample is empty
     */
    private void checkInterpolation() throws NoDataException {
        if (abscissae.isEmpty()) {
            throw new NoDataException(LocalizedFormats.EMPTY_INTERPOLATION_SAMPLE);
        }
    }

    /** Create a polynomial from its coefficients.
     * @param c polynomials coefficients
     * @return polynomial
     */
    private PolynomialFunction polynomial(double ... c) {
        return new PolynomialFunction(c);
    }

}