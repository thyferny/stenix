package analysis.integration.gauss;

import java.math.BigDecimal;
import math.util.Pair;

public class GaussIntegratorFactory {
    /** Generator of Gauss-Legendre integrators. */
    private final BaseRuleFactory<Double> legendre = new LegendreRuleFactory();
    /** Generator of Gauss-Legendre integrators. */
    private final BaseRuleFactory<BigDecimal> legendreHighPrecision = new LegendreHighPrecisionRuleFactory();

    /**
     * Creates an integrator of the given order, and whose call to the
     * {@link GaussIntegrator#integrate(org.apache.commons.math3.analysis.UnivariateFunction)
     * integrate} method will perform an integration on the natural interval
     * {@code [-1 , 1]}.
     *
     * @param numberOfPoints Order of the integration rule.
     * @return a Gauss-Legendre integrator.
     */
    public GaussIntegrator legendre(int numberOfPoints) {
        return new GaussIntegrator(getRule(legendre, numberOfPoints));
    }

    /**
     * Creates an integrator of the given order, and whose call to the
     * {@link GaussIntegrator#integrate(org.apache.commons.math3.analysis.UnivariateFunction)
     * integrate} method will perform an integration on the given interval.
     *
     * @param numberOfPoints Order of the integration rule.
     * @param lowerBound Lower bound of the integration interval.
     * @param upperBound Upper bound of the integration interval.
     * @return a Gauss-Legendre integrator.
     */
    public GaussIntegrator legendre(int numberOfPoints,
                                    double lowerBound,
                                    double upperBound) {
        return new GaussIntegrator(transform(getRule(legendre, numberOfPoints),
                                             lowerBound, upperBound));
    }

    /**
     * Creates an integrator of the given order, and whose call to the
     * {@link GaussIntegrator#integrate(org.apache.commons.math3.analysis.UnivariateFunction)
     * integrate} method will perform an integration on the natural interval
     * {@code [-1 , 1]}.
     *
     * @param numberOfPoints Order of the integration rule.
     * @return a Gauss-Legendre integrator.
     */
    public GaussIntegrator legendreHighPrecision(int numberOfPoints) {
        return new GaussIntegrator(getRule(legendreHighPrecision, numberOfPoints));
    }

    /**
     * Creates an integrator of the given order, and whose call to the
     * {@link GaussIntegrator#integrate(org.apache.commons.math3.analysis.UnivariateFunction)
     * integrate} method will perform an integration on the given interval.
     *
     * @param numberOfPoints Order of the integration rule.
     * @param lowerBound Lower bound of the integration interval.
     * @param upperBound Upper bound of the integration interval.
     * @return a Gauss-Legendre integrator.
     */
    public GaussIntegrator legendreHighPrecision(int numberOfPoints,
                                                 double lowerBound,
                                                 double upperBound) {
        return new GaussIntegrator(transform(getRule(legendreHighPrecision, numberOfPoints),
                                             lowerBound, upperBound));
    }

    /**
     * @param factory Integration rule factory.
     * @param numberOfPoints Order of the integration rule.
     * @return the integration nodes and weights.
     */
    private static Pair<double[], double[]> getRule(BaseRuleFactory<? extends Number> factory,
                                                    int numberOfPoints) {
        return factory.getRule(numberOfPoints);
    }

    /**
     * Performs a change of variable so that the integration can be performed
     * on an arbitrary interval {@code [a, b]}.
     * It is assumed that the natural interval is {@code [-1, 1]}.
     *
     * @param rule Original points and weights.
     * @param a Lower bound of the integration interval.
     * @param b Lower bound of the integration interval.
     * @return the points and weights adapted to the new interval.
     */
    private static Pair<double[], double[]> transform(Pair<double[], double[]> rule,
                                                      double a,
                                                      double b) {
        final double[] points = rule.getFirst();
        final double[] weights = rule.getSecond();

        // Scaling
        final double scale = (b - a) / 2;
        final double shift = a + scale;

        for (int i = 0; i < points.length; i++) {
            points[i] = points[i] * scale + shift;
            weights[i] *= scale;
        }

        return new Pair<double[], double[]>(points, weights);
    }
}
