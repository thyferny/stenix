package linear;

import exception.MathIllegalArgumentException;
import exception.util.LocalizedFormats;

public class SingularMatrixException extends MathIllegalArgumentException {
    /** Serializable version Id. */
    private static final long serialVersionUID = -4206514844735401070L;

    /**
     * Construct an exception.
     */
    public SingularMatrixException() {
        super(LocalizedFormats.SINGULAR_MATRIX);
    }
}
