package linear;

import exception.MathIllegalArgumentException;
import exception.util.LocalizedFormats;

public class NonPositiveDefiniteOperatorException
    extends MathIllegalArgumentException {
    /** Serializable version Id. */
    private static final long serialVersionUID = 917034489420549847L;

    /** Creates a new instance of this class. */
    public NonPositiveDefiniteOperatorException() {
        super(LocalizedFormats.NON_POSITIVE_DEFINITE_OPERATOR);
    }
}
