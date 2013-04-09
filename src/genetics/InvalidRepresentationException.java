package genetics;

import exception.MathIllegalArgumentException;
import exception.util.Localizable;

public class InvalidRepresentationException extends MathIllegalArgumentException {

    /** Serialization version id */
    private static final long serialVersionUID = 1L;

    /**
     * Construct an InvalidRepresentationException with a specialized message.
     *
     * @param pattern Message pattern.
     * @param args Arguments.
     */
    public InvalidRepresentationException(Localizable pattern, Object ... args) {
       super(pattern, args);
    }

}
