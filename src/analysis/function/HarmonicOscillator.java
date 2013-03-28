package analysis.function;

import math.util.FastMath;
import analysis.ParametricUnivariateFunction;
import analysis.differentiation.DerivativeStructure;
import analysis.differentiation.UnivariateDifferentiableFunction;
import exception.DimensionMismatchException;
import exception.NullArgumentException;


public class HarmonicOscillator implements UnivariateDifferentiableFunction {
    /** Amplitude. */
    private final double amplitude;
    /** Angular frequency. */
    private final double omega;
    /** Phase. */
    private final double phase;

    /**
     * Harmonic oscillator function.
     *
     * @param amplitude Amplitude.
     * @param omega Angular frequency.
     * @param phase Phase.
     */
    public HarmonicOscillator(double amplitude,
                              double omega,
                              double phase) {
        this.amplitude = amplitude;
        this.omega = omega;
        this.phase = phase;
    }

    /** {@inheritDoc} */
    public double value(double x) {
        return value(omega * x + phase, amplitude);
    }

    /**
     * Parametric function where the input array contains the parameters of
     * the harmonic oscillator function, ordered as follows:
     * <ul>
     *  <li>Amplitude</li>
     *  <li>Angular frequency</li>
     *  <li>Phase</li>
     * </ul>
     */
    public static class Parametric implements ParametricUnivariateFunction {
        /**
         * Computes the value of the harmonic oscillator at {@code x}.
         *
         * @param x Value for which the function must be computed.
         * @param param Values of norm, mean and standard deviation.
         * @return the value of the function.
         * @throws NullArgumentException if {@code param} is {@code null}.
         * @throws DimensionMismatchException if the size of {@code param} is
         * not 3.
         */
        public double value(double x, double ... param)
            throws NullArgumentException,
                   DimensionMismatchException {
            validateParameters(param);
            return HarmonicOscillator.value(x * param[1] + param[2], param[0]);
        }

        /**
         * Computes the value of the gradient at {@code x}.
         * The components of the gradient vector are the partial
         * derivatives of the function with respect to each of the
         * <em>parameters</em> (amplitude, angular frequency and phase).
         *
         * @param x Value at which the gradient must be computed.
         * @param param Values of amplitude, angular frequency and phase.
         * @return the gradient vector at {@code x}.
         * @throws NullArgumentException if {@code param} is {@code null}.
         * @throws DimensionMismatchException if the size of {@code param} is
         * not 3.
         */
        public double[] gradient(double x, double ... param)
            throws NullArgumentException,
                   DimensionMismatchException {
            validateParameters(param);

            final double amplitude = param[0];
            final double omega = param[1];
            final double phase = param[2];

            final double xTimesOmegaPlusPhase = omega * x + phase;
            final double a = HarmonicOscillator.value(xTimesOmegaPlusPhase, 1);
            final double p = -amplitude * FastMath.sin(xTimesOmegaPlusPhase);
            final double w = p * x;

            return new double[] { a, w, p };
        }

        /**
         * Validates parameters to ensure they are appropriate for the evaluation of
         * the {@link #value(double,double[])} and {@link #gradient(double,double[])}
         * methods.
         *
         * @param param Values of norm, mean and standard deviation.
         * @throws NullArgumentException if {@code param} is {@code null}.
         * @throws DimensionMismatchException if the size of {@code param} is
         * not 3.
         */
        private void validateParameters(double[] param)
            throws NullArgumentException,
                   DimensionMismatchException {
            if (param == null) {
                throw new NullArgumentException();
            }
            if (param.length != 3) {
                throw new DimensionMismatchException(param.length, 3);
            }
        }
    }

    /**
     * @param xTimesOmegaPlusPhase {@code omega * x + phase}.
     * @param amplitude Amplitude.
     * @return the value of the harmonic oscillator function at {@code x}.
     */
    private static double value(double xTimesOmegaPlusPhase,
                                double amplitude) {
        return amplitude * FastMath.cos(xTimesOmegaPlusPhase);
    }

    /** {@inheritDoc}
     * @since 3.1
     */
    public DerivativeStructure value(final DerivativeStructure t) {
        final double x = t.getValue();
        double[] f = new double[t.getOrder() + 1];

        final double alpha = omega * x + phase;
        f[0] = amplitude * FastMath.cos(alpha);
        if (f.length > 1) {
            f[1] = -amplitude * omega * FastMath.sin(alpha);
            final double mo2 = - omega * omega;
            for (int i = 2; i < f.length; ++i) {
                f[i] = mo2 * f[i - 2];
            }
        }

        return t.compose(f);

    }

}
