package linear;

import exception.MathIllegalArgumentException;
import exception.util.LocalizedFormats;

public class NonSelfAdjointOperatorException
    extends MathIllegalArgumentException {
    /** Serializable version Id. */
    private static final long serialVersionUID = 1784999305030258247L;

    /** Creates a new instance of this class. */
    public NonSelfAdjointOperatorException() {
        super(LocalizedFormats.NON_SELF_ADJOINT_OPERATOR);
    }
}
