package ode;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import exception.DimensionMismatchException;
import exception.MaxCountExceededException;

class ParameterJacobianWrapper implements ParameterJacobianProvider {

    /** Main ODE set. */
    private final FirstOrderDifferentialEquations fode;

    /** Raw ODE without Jacobian computation skill to be wrapped into a ParameterJacobianProvider. */
    private final ParameterizedODE pode;

    /** Steps for finite difference computation of the Jacobian df/dp w.r.t. parameters. */
    private final Map<String, Double> hParam;

    /** Wrap a {@link ParameterizedODE} into a {@link ParameterJacobianProvider}.
     * @param fode main first order differential equations set
     * @param pode secondary problem, without parameter Jacobian computation skill
     * @param paramsAndSteps parameters and steps to compute the Jacobians df/dp
     * @see JacobianMatrices#setParameterStep(String, double)
     */
    public ParameterJacobianWrapper(final FirstOrderDifferentialEquations fode,
                                    final ParameterizedODE pode,
                                    final ParameterConfiguration[] paramsAndSteps) {
        this.fode = fode;
        this.pode = pode;
        this.hParam = new HashMap<String, Double>();

        // set up parameters for jacobian computation
        for (final ParameterConfiguration param : paramsAndSteps) {
            final String name = param.getParameterName();
            if (pode.isSupported(name)) {
                hParam.put(name, param.getHP());
            }
        }
    }

    /** {@inheritDoc} */
    public Collection<String> getParametersNames() {
        return pode.getParametersNames();
    }

    /** {@inheritDoc} */
    public boolean isSupported(String name) {
        return pode.isSupported(name);
    }

    /** {@inheritDoc} */
    public void computeParameterJacobian(double t, double[] y, double[] yDot,
                                         String paramName, double[] dFdP)
        throws DimensionMismatchException, MaxCountExceededException {

        final int n = fode.getDimension();
        if (pode.isSupported(paramName)) {
            final double[] tmpDot = new double[n];

            // compute the jacobian df/dp w.r.t. parameter
            final double p  = pode.getParameter(paramName);
            final double hP = hParam.get(paramName);
            pode.setParameter(paramName, p + hP);
            fode.computeDerivatives(t, y, tmpDot);
            for (int i = 0; i < n; ++i) {
                dFdP[i] = (tmpDot[i] - yDot[i]) / hP;
            }
            pode.setParameter(paramName, p);
        } else {
            Arrays.fill(dFdP, 0, n, 0.0);
        }

    }

}
