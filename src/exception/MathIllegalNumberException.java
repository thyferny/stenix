package exception;

import exception.util.Localizable;

public class MathIllegalNumberException extends MathIllegalArgumentException {
    /** Serializable version Id. */
    private static final long serialVersionUID = -7447085893598031110L;
    /** Requested. */
    private final Number argument;

    /**
     * Construct an exception.
     *
     * @param pattern Localizable pattern.
     * @param wrong Wrong number.
     * @param arguments Arguments.
     */
    protected MathIllegalNumberException(Localizable pattern,
                                         Number wrong,
                                         Object ... arguments) {
        super(pattern, wrong, arguments);
        argument = wrong;
    }

    /**
     * @return the requested value.
     */
    public Number getArgument() {
        return argument;
    }
}
