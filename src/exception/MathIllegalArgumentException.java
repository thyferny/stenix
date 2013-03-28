package exception;

import exception.util.ExceptionContext;
import exception.util.ExceptionContextProvider;
import exception.util.Localizable;

public class MathIllegalArgumentException extends IllegalArgumentException
    implements ExceptionContextProvider {
    /** Serializable version Id. */
    private static final long serialVersionUID = -6024911025449780478L;
    /** Context. */
    private final ExceptionContext context;

    /**
     * @param pattern Message pattern explaining the cause of the error.
     * @param args Arguments.
     */
    public MathIllegalArgumentException(Localizable pattern,
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
