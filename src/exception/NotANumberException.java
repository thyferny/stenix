package exception;

import exception.util.LocalizedFormats;

public class NotANumberException extends MathIllegalNumberException {
    /** Serializable version Id. */
    private static final long serialVersionUID = 20120906L;

    /**
     * Construct the exception.
     */
    public NotANumberException() {
        super(LocalizedFormats.NAN_NOT_ALLOWED, Double.NaN);
    }

}
