package optim.linear;

import exception.MathIllegalStateException;
import exception.util.LocalizedFormats;

public class NoFeasibleSolutionException extends MathIllegalStateException {
    /** Serializable version identifier. */
    private static final long serialVersionUID = -3044253632189082760L;

    /**
     * Simple constructor using a default message.
     */
    public NoFeasibleSolutionException() {
        super(LocalizedFormats.NO_FEASIBLE_SOLUTION);
    }
}
