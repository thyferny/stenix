package analysis.integration.gauss;

import math.util.Pair;
import exception.NotStrictlyPositiveException;
import exception.util.LocalizedFormats;

public class LegendreRuleFactory extends BaseRuleFactory<Double> {
    /**
     * {@inheritDoc}
     */
    @Override
    protected Pair<Double[], Double[]> computeRule(int numberOfPoints)
        throws NotStrictlyPositiveException {
        if (numberOfPoints <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.NUMBER_OF_POINTS,
                                                   numberOfPoints);
        }

        if (numberOfPoints == 1) {
            // Break recursion.
            return new Pair<Double[], Double[]>(new Double[] { 0d },
                                                new Double[] { 2d });
        }

        // Get previous rule.
        // If it has not been computed yet it will trigger a recursive call
        // to this method.
        final Double[] previousPoints = getRuleInternal(numberOfPoints - 1).getFirst();

        // Compute next rule.
        final Double[] points = new Double[numberOfPoints];
        final Double[] weights = new Double[numberOfPoints];

        // Find i-th root of P[n+1] by bracketing.
        final int iMax = numberOfPoints / 2;
        for (int i = 0; i < iMax; i++) {
            // Lower-bound of the interval.
            double a = (i == 0) ? -1 : previousPoints[i - 1].doubleValue();
            // Upper-bound of the interval.
            double b = (iMax == 1) ? 1 : previousPoints[i].doubleValue();
            // P[j-1](a)
            double pma = 1;
            // P[j](a)
            double pa = a;
            // P[j-1](b)
            double pmb = 1;
            // P[j](b)
            double pb = b;
            for (int j = 1; j < numberOfPoints; j++) {
                final int two_j_p_1 = 2 * j + 1;
                final int j_p_1 = j + 1;
                // P[j+1](a)
                final double ppa = (two_j_p_1 * a * pa - j * pma) / j_p_1;
                // P[j+1](b)
                final double ppb = (two_j_p_1 * b * pb - j * pmb) / j_p_1;
                pma = pa;
                pa = ppa;
                pmb = pb;
                pb = ppb;
            }
            // Now pa = P[n+1](a), and pma = P[n](a) (same holds for b).
            // Middle of the interval.
            double c = 0.5 * (a + b);
            // P[j-1](c)
            double pmc = 1;
            // P[j](c)
            double pc = c;
            boolean done = false;
            while (!done) {
                done = b - a <= Math.ulp(c);
                pmc = 1;
                pc = c;
                for (int j = 1; j < numberOfPoints; j++) {
                    // P[j+1](c)
                    final double ppc = ((2 * j + 1) * c * pc - j * pmc) / (j + 1);
                    pmc = pc;
                    pc = ppc;
                }
                // Now pc = P[n+1](c) and pmc = P[n](c).
                if (!done) {
                    if (pa * pc <= 0) {
                        b = c;
                        pmb = pmc;
                        pb = pc;
                    } else {
                        a = c;
                        pma = pmc;
                        pa = pc;
                    }
                    c = 0.5 * (a + b);
                }
            }
            final double d = numberOfPoints * (pmc - c * pc);
            final double w = 2 * (1 - c * c) / (d * d);

            points[i] = c;
            weights[i] = w;

            final int idx = numberOfPoints - i - 1;
            points[idx] = -c;
            weights[idx] = w;
        }
        // If "numberOfPoints" is odd, 0 is a root.
        // Note: as written, the test for oddness will work for negative
        // integers too (although it is not necessary here), preventing
        // a FindBugs warning.
        if (numberOfPoints % 2 != 0) {
            double pmc = 1;
            for (int j = 1; j < numberOfPoints; j += 2) {
                pmc = -j * pmc / (j + 1);
            }
            final double d = numberOfPoints * pmc;
            final double w = 2 / (d * d);

            points[iMax] = 0d;
            weights[iMax] = w;
        }

        return new Pair<Double[], Double[]>(points, weights);
    }
}
