package ode;

import java.util.ArrayList;
import java.util.Collection;

public abstract class AbstractParameterizable implements Parameterizable {

   /** List of the parameters names. */
    private final Collection<String> parametersNames;

    /** Simple constructor.
     * @param names names of the supported parameters
     */
    protected AbstractParameterizable(final String ... names) {
        parametersNames = new ArrayList<String>();
        for (final String name : names) {
            parametersNames.add(name);
        }
    }

    /** Simple constructor.
     * @param names names of the supported parameters
     */
    protected AbstractParameterizable(final Collection<String> names) {
        parametersNames = new ArrayList<String>();
        parametersNames.addAll(names);
    }

    /** {@inheritDoc} */
    public Collection<String> getParametersNames() {
        return parametersNames;
    }

    /** {@inheritDoc} */
    public boolean isSupported(final String name) {
        for (final String supportedName : parametersNames) {
            if (supportedName.equals(name)) {
                return true;
            }
        }
        return false;
    }

    /** Check if a parameter is supported and throw an IllegalArgumentException if not.
     * @param name name of the parameter to check
     * @exception UnknownParameterException if the parameter is not supported
     * @see #isSupported(String)
     */
    public void complainIfNotSupported(final String name)
        throws UnknownParameterException {
        if (!isSupported(name)) {
            throw new UnknownParameterException(name);
        }
    }

}
