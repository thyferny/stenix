package complex;

import math.util.FastMath;
import exception.MathIllegalArgumentException;
import exception.util.LocalizedFormats;

public class ComplexUtils {

    /**
     * Default constructor.
     */
    private ComplexUtils() {}

    /**
     * Creates a complex number from the given polar representation.
     * <p>
     * The value returned is <code>r&middot;e<sup>i&middot;theta</sup></code>,
     * computed as <code>r&middot;cos(theta) + r&middot;sin(theta)i</code></p>
     * <p>
     * If either <code>r</code> or <code>theta</code> is NaN, or
     * <code>theta</code> is infinite, {@link Complex#NaN} is returned.</p>
     * <p>
     * If <code>r</code> is infinite and <code>theta</code> is finite,
     * infinite or NaN values may be returned in parts of the result, following
     * the rules for double arithmetic.<pre>
     * Examples:
     * <code>
     * polar2Complex(INFINITY, &pi;/4) = INFINITY + INFINITY i
     * polar2Complex(INFINITY, 0) = INFINITY + NaN i
     * polar2Complex(INFINITY, -&pi;/4) = INFINITY - INFINITY i
     * polar2Complex(INFINITY, 5&pi;/4) = -INFINITY - INFINITY i </code></pre></p>
     *
     * @param r the modulus of the complex number to create
     * @param theta  the argument of the complex number to create
     * @return <code>r&middot;e<sup>i&middot;theta</sup></code>
     * @throws MathIllegalArgumentException if {@code r} is negative.
     * @since 1.1
     */
    public static Complex polar2Complex(double r, double theta) throws MathIllegalArgumentException {
        if (r < 0) {
            throw new MathIllegalArgumentException(
                  LocalizedFormats.NEGATIVE_COMPLEX_MODULE, r);
        }
        return new Complex(r * FastMath.cos(theta), r * FastMath.sin(theta));
    }

    /**
     * Convert an array of primitive doubles to an array of {@code Complex} objects.
     *
     * @param real Array of numbers to be converted to their {@code Complex}
     * equivalent.
     * @return an array of {@code Complex} objects.
     *
     * @since 3.1
     */
    public static Complex[] convertToComplex(double[] real) {
        final Complex c[] = new Complex[real.length];
        for (int i = 0; i < real.length; i++) {
            c[i] = new Complex(real[i], 0);
        }

        return c;
    }
}
