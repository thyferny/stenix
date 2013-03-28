package linear;

import exception.MathIllegalArgumentException;
import exception.util.LocalizedFormats;

public class SingularOperatorException
    extends MathIllegalArgumentException {
    /** Serializable version Id. */
    private static final long serialVersionUID = -476049978595245033L;

    /**
     * Creates a new instance of this class.
     */
    public SingularOperatorException() {
        super(LocalizedFormats.SINGULAR_OPERATOR);
    }
}
