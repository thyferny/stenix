package optim.linear;

import exception.MathIllegalStateException;
import exception.util.LocalizedFormats;

public class UnboundedSolutionException extends MathIllegalStateException {
    /** Serializable version identifier. */
    private static final long serialVersionUID = 940539497277290619L;

    /**
     * Simple constructor using a default message.
     */
    public UnboundedSolutionException() {
        super(LocalizedFormats.UNBOUNDED_SOLUTION);
    }
}
