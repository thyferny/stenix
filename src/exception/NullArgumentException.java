package exception;

import exception.util.Localizable;
import exception.util.LocalizedFormats;

public class NullArgumentException extends MathIllegalArgumentException {
    /** Serializable version Id. */
    private static final long serialVersionUID = -6024911025449780478L;

    /**
     * Default constructor.
     */
    public NullArgumentException() {
        this(LocalizedFormats.NULL_NOT_ALLOWED);
    }
    /**
     * @param pattern Message pattern providing the specific context of
     * the error.
     * @param arguments Values for replacing the placeholders in {@code pattern}.
     */
    public NullArgumentException(Localizable pattern,
                                 Object ... arguments) {
        super(pattern, arguments);
    }
}
