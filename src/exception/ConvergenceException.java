package exception;

import exception.util.Localizable;
import exception.util.LocalizedFormats;

public class ConvergenceException extends MathIllegalStateException {
    /** Serializable version Id. */
    private static final long serialVersionUID = 4330003017885151975L;

    /**
     * Construct the exception.
     */
    public ConvergenceException() {
        this(LocalizedFormats.CONVERGENCE_FAILED);
    }

    /**
     * Construct the exception with a specific context and arguments.
     *
     * @param pattern Message pattern providing the specific context of
     * the error.
     * @param args Arguments.
     */
    public ConvergenceException(Localizable pattern,
                                Object ... args) {
        getContext().addMessage(pattern, args);
    }
}
