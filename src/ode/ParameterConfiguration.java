package ode;

import java.io.Serializable;

class ParameterConfiguration implements Serializable {

    /** Serializable UID. */
    private static final long serialVersionUID = 2247518849090889379L;

    /** Parameter name. */
    private String parameterName;

    /** Parameter step for finite difference computation. */
    private double hP;

    /** Parameter name and step pair constructor.
     * @param parameterName parameter name
     * @param hP parameter step
     */
    public ParameterConfiguration(final String parameterName, final double hP) {
        this.parameterName = parameterName;
        this.hP = hP;
    }

    /** Get parameter name.
     * @return parameterName parameter name
     */
    public String getParameterName() {
        return parameterName;
    }

    /** Get parameter step.
     * @return hP parameter step
     */
    public double getHP() {
        return hP;
    }

    /** Set parameter step.
     * @param hParam parameter step
     */
    public void setHP(final double hParam) {
        this.hP = hParam;
    }

}
