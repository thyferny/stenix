package exception;

import exception.util.Localizable;
import exception.util.LocalizedFormats;


public class NoBracketingException extends MathIllegalArgumentException {
    /** Serializable version Id. */
    private static final long serialVersionUID = -3629324471511904459L;
    /** Lower end of the interval. */
    private final double lo;
    /** Higher end of the interval. */
    private final double hi;
    /** Value at lower end of the interval. */
    private final double fLo;
    /** Value at higher end of the interval. */
    private final double fHi;

    /**
     * Construct the exception.
     *
     * @param lo Lower end of the interval.
     * @param hi Higher end of the interval.
     * @param fLo Value at lower end of the interval.
     * @param fHi Value at higher end of the interval.
     */
    public NoBracketingException(double lo, double hi,
                                 double fLo, double fHi) {
        this(LocalizedFormats.SAME_SIGN_AT_ENDPOINTS, lo, hi, fLo, fHi);
    }

    /**
     * Construct the exception with a specific context.
     *
     * @param specific Contextual information on what caused the exception.
     * @param lo Lower end of the interval.
     * @param hi Higher end of the interval.
     * @param fLo Value at lower end of the interval.
     * @param fHi Value at higher end of the interval.
     * @param args Additional arguments.
     */
    public NoBracketingException(Localizable specific,
                                 double lo, double hi,
                                 double fLo, double fHi,
                                 Object ... args) {
        super(specific, lo, hi, fLo, fHi, args);
        this.lo = lo;
        this.hi = hi;
        this.fLo = fLo;
        this.fHi = fHi;
    }

    /**
     * Get the lower end of the interval.
     *
     * @return the lower end.
     */
    public double getLo() {
        return lo;
    }
    /**
     * Get the higher end of the interval.
     *
     * @return the higher end.
     */
    public double getHi() {
        return hi;
    }
    /**
     * Get the value at the lower end of the interval.
     *
     * @return the value at the lower end.
     */
    public double getFLo() {
        return fLo;
    }
    /**
     * Get the value at the higher end of the interval.
     *
     * @return the value at the higher end.
     */
    public double getFHi() {
        return fHi;
    }
}
