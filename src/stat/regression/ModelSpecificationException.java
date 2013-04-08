package stat.regression;

import exception.MathIllegalArgumentException;
import exception.util.Localizable;


public class ModelSpecificationException extends MathIllegalArgumentException {
    /** Serializable version Id. */
    private static final long serialVersionUID = 4206514456095401070L;

    /**
     * @param pattern message pattern describing the specification error.
     *
     * @param args arguments.
     */
    public ModelSpecificationException(Localizable pattern,
                                        Object ... args) {
        super(pattern, args);
    }
}
