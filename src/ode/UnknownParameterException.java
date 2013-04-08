package ode;

import exception.MathIllegalArgumentException;
import exception.util.LocalizedFormats;

public class UnknownParameterException extends MathIllegalArgumentException {

    /** Serializable version Id. */
    private static final long serialVersionUID = 20120902L;

    /** Parameter name. */
    private final String name;

    /**
     * Construct an exception from the unknown parameter.
     *
     * @param name parameter name.
     */
    public UnknownParameterException(final String name) {
        super(LocalizedFormats.UNKNOWN_PARAMETER);
        this.name = name;
    }

    /**
     * @return the name of the unknown parameter.
     */
    public String getName() {
        return name;
    }

}
