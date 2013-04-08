package ode;

import java.util.Collection;

public interface Parameterizable {

    /** Get the names of the supported parameters.
     * @return parameters names
     * @see #isSupported(String)
     */
    Collection<String> getParametersNames();

    /** Check if a parameter is supported.
     * <p>Supported parameters are those listed by {@link #getParametersNames()}.</p>
     * @param name parameter name to check
     * @return true if the parameter is supported
     * @see #getParametersNames()
     */
    boolean isSupported(String name);

}
