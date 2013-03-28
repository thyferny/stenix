package analysis.differentiation;

import java.io.Serializable;

import math.util.FastMath;
import analysis.UnivariateFunction;
import analysis.UnivariateMatrixFunction;
import analysis.UnivariateVectorFunction;
import exception.MathIllegalArgumentException;
import exception.NotPositiveException;
import exception.NumberIsTooLargeException;
import exception.NumberIsTooSmallException;


public class FiniteDifferencesDifferentiator
    implements UnivariateFunctionDifferentiator, UnivariateVectorFunctionDifferentiator,
               UnivariateMatrixFunctionDifferentiator, Serializable {

    /** Serializable UID. */
    private static final long serialVersionUID = 20120917L;

    /** Number of points to use. */
    private final int nbPoints;

    /** Step size. */
    private final double stepSize;

    /** Half sample span. */
    private final double halfSampleSpan;

    /** Lower bound for independent variable. */
    private final double tMin;

    /** Upper bound for independent variable. */
    private final double tMax;

    /**
     * Build a differentiator with number of points and step size when independent variable is unbounded.
     * <p>
     * Beware that wrong settings for the finite differences differentiator
     * can lead to highly unstable and inaccurate results, especially for
     * high derivation orders. Using very small step sizes is often a
     * <em>bad</em> idea.
     * </p>
     * @param nbPoints number of points to use
     * @param stepSize step size (gap between each point)
     * @exception NotPositiveException if {@code stepsize <= 0} (note that
     * {@link NotPositiveException} extends {@link NumberIsTooSmallException})
     * @exception NumberIsTooSmallException {@code nbPoint <= 1}
     */
    public FiniteDifferencesDifferentiator(final int nbPoints, final double stepSize)
        throws NotPositiveException, NumberIsTooSmallException {
        this(nbPoints, stepSize, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
    }

    /**
     * Build a differentiator with number of points and step size when independent variable is bounded.
     * <p>
     * When the independent variable is bounded (tLower &lt; t &lt; tUpper), the sampling
     * points used for differentiation will be adapted to ensure the constraint holds
     * even near the boundaries. This means the sample will not be centered anymore in
     * these cases. At an extreme case, computing derivatives exactly at the lower bound
     * will lead the sample to be entirely on the right side of the derivation point.
     * </p>
     * <p>
     * Note that the boundaries are considered to be excluded for function evaluation.
     * </p>
     * <p>
     * Beware that wrong settings for the finite differences differentiator
     * can lead to highly unstable and inaccurate results, especially for
     * high derivation orders. Using very small step sizes is often a
     * <em>bad</em> idea.
     * </p>
     * @param nbPoints number of points to use
     * @param stepSize step size (gap between each point)
     * @param tLower lower bound for independent variable (may be {@code Double.NEGATIVE_INFINITY}
     * if there are no lower bounds)
     * @param tUpper upper bound for independent variable (may be {@code Double.POSITIVE_INFINITY}
     * if there are no upper bounds)
     * @exception NotPositiveException if {@code stepsize <= 0} (note that
     * {@link NotPositiveException} extends {@link NumberIsTooSmallException})
     * @exception NumberIsTooSmallException {@code nbPoint <= 1}
     * @exception NumberIsTooLargeException {@code stepSize * (nbPoints - 1) >= tUpper - tLower}
     */
    public FiniteDifferencesDifferentiator(final int nbPoints, final double stepSize,
                                           final double tLower, final double tUpper)
            throws NotPositiveException, NumberIsTooSmallException, NumberIsTooLargeException {

        if (nbPoints <= 1) {
            throw new NumberIsTooSmallException(stepSize, 1, false);
        }
        this.nbPoints = nbPoints;

        if (stepSize <= 0) {
            throw new NotPositiveException(stepSize);
        }
        this.stepSize = stepSize;

        halfSampleSpan = 0.5 * stepSize * (nbPoints - 1);
        if (2 * halfSampleSpan >= tUpper - tLower) {
            throw new NumberIsTooLargeException(2 * halfSampleSpan, tUpper - tLower, false);
        }
        final double safety = FastMath.ulp(halfSampleSpan);
        this.tMin = tLower + halfSampleSpan + safety;
        this.tMax = tUpper - halfSampleSpan - safety;

    }

    /**
     * Get the number of points to use.
     * @return number of points to use
     */
    public int getNbPoints() {
        return nbPoints;
    }

    /**
     * Get the step size.
     * @return step size
     */
    public double getStepSize() {
        return stepSize;
    }

    /**
     * Evaluate derivatives from a sample.
     * <p>
     * Evaluation is done using divided differences.
     * </p>
     * @param t evaluation abscissa value and derivatives
     * @param t0 first sample point abscissa
     * @param y function values sample {@code y[i] = f(t[i]) = f(t0 + i * stepSize)}
     * @return value and derivatives at {@code t}
     * @exception NumberIsTooLargeException if the requested derivation order
     * is larger or equal to the number of points
     */
    private DerivativeStructure evaluate(final DerivativeStructure t, final double t0,
                                         final double[] y)
        throws NumberIsTooLargeException {

        // create divided differences diagonal arrays
        final double[] top    = new double[nbPoints];
        final double[] bottom = new double[nbPoints];

        for (int i = 0; i < nbPoints; ++i) {

            // update the bottom diagonal of the divided differences array
            bottom[i] = y[i];
            for (int j = 1; j <= i; ++j) {
                bottom[i - j] = (bottom[i - j + 1] - bottom[i - j]) / (j * stepSize);
            }

            // update the top diagonal of the divided differences array
            top[i] = bottom[0];

        }

        // evaluate interpolation polynomial (represented by top diagonal) at t
        final int order            = t.getOrder();
        final int parameters       = t.getFreeParameters();
        final double[] derivatives = t.getAllDerivatives();
        final double dt0           = t.getValue() - t0;
        DerivativeStructure interpolation = new DerivativeStructure(parameters, order, 0.0);
        DerivativeStructure monomial = null;
        for (int i = 0; i < nbPoints; ++i) {
            if (i == 0) {
                // start with monomial(t) = 1
                monomial = new DerivativeStructure(parameters, order, 1.0);
            } else {
                // monomial(t) = (t - t0) * (t - t1) * ... * (t - t(i-1))
                derivatives[0] = dt0 - (i - 1) * stepSize;
                final DerivativeStructure deltaX = new DerivativeStructure(parameters, order, derivatives);
                monomial = monomial.multiply(deltaX);
            }
            interpolation = interpolation.add(monomial.multiply(top[i]));
        }

        return interpolation;

    }

    /** {@inheritDoc}
     * <p>The returned object cannot compute derivatives to arbitrary orders. The
     * value function will throw a {@link NumberIsTooLargeException} if the requested
     * derivation order is larger or equal to the number of points.
     * </p>
     */
    public UnivariateDifferentiableFunction differentiate(final UnivariateFunction function) {
        return new UnivariateDifferentiableFunction() {

            /** {@inheritDoc} */
            public double value(final double x) throws MathIllegalArgumentException {
                return function.value(x);
            }

            /** {@inheritDoc} */
            public DerivativeStructure value(final DerivativeStructure t)
                throws MathIllegalArgumentException {

                // check we can achieve the requested derivation order with the sample
                if (t.getOrder() >= nbPoints) {
                    throw new NumberIsTooLargeException(t.getOrder(), nbPoints, false);
                }

                // compute sample position, trying to be centered if possible
                final double t0 = FastMath.max(FastMath.min(t.getValue(), tMax), tMin) - halfSampleSpan;

                // compute sample points
                final double[] y = new double[nbPoints];
                for (int i = 0; i < nbPoints; ++i) {
                    y[i] = function.value(t0 + i * stepSize);
                }

                // evaluate derivatives
                return evaluate(t, t0, y);

            }

        };
    }

    /** {@inheritDoc}
     * <p>The returned object cannot compute derivatives to arbitrary orders. The
     * value function will throw a {@link NumberIsTooLargeException} if the requested
     * derivation order is larger or equal to the number of points.
     * </p>
     */
    public UnivariateDifferentiableVectorFunction differentiate(final UnivariateVectorFunction function) {
        return new UnivariateDifferentiableVectorFunction() {

            /** {@inheritDoc} */
            public double[]value(final double x) throws MathIllegalArgumentException {
                return function.value(x);
            }

            /** {@inheritDoc} */
            public DerivativeStructure[] value(final DerivativeStructure t)
                throws MathIllegalArgumentException {

                // check we can achieve the requested derivation order with the sample
                if (t.getOrder() >= nbPoints) {
                    throw new NumberIsTooLargeException(t.getOrder(), nbPoints, false);
                }

                // compute sample position, trying to be centered if possible
                final double t0 = FastMath.max(FastMath.min(t.getValue(), tMax), tMin) - halfSampleSpan;

                // compute sample points
                double[][] y = null;
                for (int i = 0; i < nbPoints; ++i) {
                    final double[] v = function.value(t0 + i * stepSize);
                    if (i == 0) {
                        y = new double[v.length][nbPoints];
                    }
                    for (int j = 0; j < v.length; ++j) {
                        y[j][i] = v[j];
                    }
                }

                // evaluate derivatives
                final DerivativeStructure[] value = new DerivativeStructure[y.length];
                for (int j = 0; j < value.length; ++j) {
                    value[j] = evaluate(t, t0, y[j]);
                }

                return value;

            }

        };
    }

    /** {@inheritDoc}
     * <p>The returned object cannot compute derivatives to arbitrary orders. The
     * value function will throw a {@link NumberIsTooLargeException} if the requested
     * derivation order is larger or equal to the number of points.
     * </p>
     */
    public UnivariateDifferentiableMatrixFunction differentiate(final UnivariateMatrixFunction function) {
        return new UnivariateDifferentiableMatrixFunction() {

            /** {@inheritDoc} */
            public double[][]  value(final double x) throws MathIllegalArgumentException {
                return function.value(x);
            }

            /** {@inheritDoc} */
            public DerivativeStructure[][]  value(final DerivativeStructure t)
                throws MathIllegalArgumentException {

                // check we can achieve the requested derivation order with the sample
                if (t.getOrder() >= nbPoints) {
                    throw new NumberIsTooLargeException(t.getOrder(), nbPoints, false);
                }

                // compute sample position, trying to be centered if possible
                final double t0 = FastMath.max(FastMath.min(t.getValue(), tMax), tMin) - halfSampleSpan;

                // compute sample points
                double[][][] y = null;
                for (int i = 0; i < nbPoints; ++i) {
                    final double[][] v = function.value(t0 + i * stepSize);
                    if (i == 0) {
                        y = new double[v.length][v[0].length][nbPoints];
                    }
                    for (int j = 0; j < v.length; ++j) {
                        for (int k = 0; k < v[j].length; ++k) {
                            y[j][k][i] = v[j][k];
                        }
                    }
                }

                // evaluate derivatives
                final DerivativeStructure[][] value = new DerivativeStructure[y.length][y[0].length];
                for (int j = 0; j < value.length; ++j) {
                    for (int k = 0; k < y[j].length; ++k) {
                        value[j][k] = evaluate(t, t0, y[j][k]);
                    }
                }

                return value;

            }

        };
    }

}
