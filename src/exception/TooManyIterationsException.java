package exception;

import exception.util.LocalizedFormats;

public class TooManyIterationsException extends MaxCountExceededException {
    /** Serializable version Id. */
    private static final long serialVersionUID = 20121211L;

    /**
     * Construct the exception.
     *
     * @param max Maximum number of evaluations.
     */
    public TooManyIterationsException(Number max) {
        super(max);
        getContext().addMessage(LocalizedFormats.ITERATIONS);
    }
}
