package exception;

import exception.util.LocalizedFormats;

public class TooManyEvaluationsException extends MaxCountExceededException {
    /** Serializable version Id. */
    private static final long serialVersionUID = 4330003017885151975L;

    /**
     * Construct the exception.
     *
     * @param max Maximum number of evaluations.
     */
    public TooManyEvaluationsException(Number max) {
        super(max);
        getContext().addMessage(LocalizedFormats.EVALUATIONS);
    }
}
