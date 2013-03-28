package exception;
import exception.util.ExceptionContext;
import exception.util.ExceptionContextProvider;
import exception.util.Localizable;
import exception.util.LocalizedFormats;

public class MathUnsupportedOperationException extends UnsupportedOperationException
    implements ExceptionContextProvider {
    /** Serializable version Id. */
    private static final long serialVersionUID = -6024911025449780478L;
    /** Context. */
    private final ExceptionContext context;

    /**
     * Default constructor.
     */
    public MathUnsupportedOperationException() {
        this(LocalizedFormats.UNSUPPORTED_OPERATION);
    }
    /**
     * @param pattern Message pattern providing the specific context of
     * the error.
     * @param args Arguments.
     */
    public MathUnsupportedOperationException(Localizable pattern,
                                             Object ... args) {
        context = new ExceptionContext(this);
        context.addMessage(pattern, args);
    }

    /** {@inheritDoc} */
    public ExceptionContext getContext() {
        return context;
    }

    /** {@inheritDoc} */
    @Override
    public String getMessage() {
        return context.getMessage();
    }

    /** {@inheritDoc} */
    @Override
    public String getLocalizedMessage() {
        return context.getLocalizedMessage();
    }
}
