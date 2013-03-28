package linear;

import exception.MathIllegalArgumentException;
import exception.util.LocalizedFormats;

public class IllConditionedOperatorException
    extends MathIllegalArgumentException {
    /** Serializable version Id. */
    private static final long serialVersionUID = -7883263944530490135L;

    /**
     * Creates a new instance of this class.
     *
     * @param cond An estimate of the condition number of the offending linear
     * operator.
     */
    public IllConditionedOperatorException(final double cond) {
        super(LocalizedFormats.ILL_CONDITIONED_OPERATOR, cond);
    }
}
