package exception;

import exception.util.Localizable;
import exception.util.LocalizedFormats;

public class NotFiniteNumberException extends MathIllegalNumberException {
    /** Serializable version Id. */
    private static final long serialVersionUID = -6100997100383932834L;

    /**
     * Construct the exception.
     *
     * @param wrong Value that is infinite or NaN.
     * @param args Optional arguments.
     */
    public NotFiniteNumberException(Number wrong,
                                    Object ... args) {
        this(LocalizedFormats.NOT_FINITE_NUMBER, wrong, args);
    }

    /**
     * Construct the exception with a specific context.
     *
     * @param specific Specific context pattern.
     * @param wrong Value that is infinite or NaN.
     * @param args Optional arguments.
     */
    public NotFiniteNumberException(Localizable specific,
                                    Number wrong,
                                    Object ... args) {
        super(specific, wrong, args);
    }
}
