package random;

import math.util.FastMath;
import exception.NullArgumentException;
import exception.OutOfRangeException;
import exception.util.LocalizedFormats;

public class StableRandomGenerator implements NormalizedRandomGenerator {
    /** Underlying generator. */
    private final RandomGenerator generator;

    /** stability parameter */
    private final double alpha;

    /** skewness parameter */
    private final double beta;

    /** cache of expression value used in generation */
    private final double zeta;

    /**
     * Create a new generator.
     *
     * @param generator underlying random generator to use
     * @param alpha Stability parameter. Must be in range (0, 2]
     * @param beta Skewness parameter. Must be in range [-1, 1]
     * @throws NullArgumentException if generator is null
     * @throws OutOfRangeException if {@code alpha <= 0} or {@code alpha > 2}
     * or {@code beta < -1} or {@code beta > 1}
     */
    public StableRandomGenerator(final RandomGenerator generator,
                                 final double alpha, final double beta)
        throws NullArgumentException, OutOfRangeException {
        if (generator == null) {
            throw new NullArgumentException();
        }

        if (!(alpha > 0d && alpha <= 2d)) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_RANGE_LEFT,
                    alpha, 0, 2);
        }

        if (!(beta >= -1d && beta <= 1d)) {
            throw new OutOfRangeException(LocalizedFormats.OUT_OF_RANGE_SIMPLE,
                    beta, -1, 1);
        }

        this.generator = generator;
        this.alpha = alpha;
        this.beta = beta;
        if (alpha < 2d && beta != 0d) {
            zeta = beta * FastMath.tan(FastMath.PI * alpha / 2);
        } else {
            zeta = 0d;
        }
    }

    /**
     * Generate a random scalar with zero location and unit scale.
     *
     * @return a random scalar with zero location and unit scale
     */
    public double nextNormalizedDouble() {
        // we need 2 uniform random numbers to calculate omega and phi
        double omega = -FastMath.log(generator.nextDouble());
        double phi = FastMath.PI * (generator.nextDouble() - 0.5);

        // Normal distribution case (Box-Muller algorithm)
        if (alpha == 2d) {
            return FastMath.sqrt(2d * omega) * FastMath.sin(phi);
        }

        double x;
        // when beta = 0, zeta is zero as well
        // Thus we can exclude it from the formula
        if (beta == 0d) {
            // Cauchy distribution case
            if (alpha == 1d) {
                x = FastMath.tan(phi);
            } else {
                x = FastMath.pow(omega * FastMath.cos((1 - alpha) * phi),
                    1d / alpha - 1d) *
                    FastMath.sin(alpha * phi) /
                    FastMath.pow(FastMath.cos(phi), 1d / alpha);
            }
        } else {
            // Generic stable distribution
            double cosPhi = FastMath.cos(phi);
            // to avoid rounding errors around alpha = 1
            if (FastMath.abs(alpha - 1d) > 1e-8) {
                double alphaPhi = alpha * phi;
                double invAlphaPhi = phi - alphaPhi;
                x = (FastMath.sin(alphaPhi) + zeta * FastMath.cos(alphaPhi)) / cosPhi *
                    (FastMath.cos(invAlphaPhi) + zeta * FastMath.sin(invAlphaPhi)) /
                     FastMath.pow(omega * cosPhi, (1 - alpha) / alpha);
            } else {
                double betaPhi = FastMath.PI / 2 + beta * phi;
                x = 2d / FastMath.PI * (betaPhi * FastMath.tan(phi) - beta *
                    FastMath.log(FastMath.PI / 2d * omega * cosPhi / betaPhi));

                if (alpha != 1d) {
                    x = x + beta * FastMath.tan(FastMath.PI * alpha / 2);
                }
            }
        }
        return x;
    }
}
