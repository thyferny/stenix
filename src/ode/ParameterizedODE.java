package ode;

public interface ParameterizedODE extends Parameterizable {

    /** Get parameter value from its name.
     * @param name parameter name
     * @return parameter value
     * @exception UnknownParameterException if parameter is not supported
     */
    double getParameter(String name) throws UnknownParameterException;

    /** Set the value for a given parameter.
     * @param name parameter name
     * @param value parameter value
     * @exception UnknownParameterException if parameter is not supported
     */
    void setParameter(String name, double value) throws UnknownParameterException;

}
