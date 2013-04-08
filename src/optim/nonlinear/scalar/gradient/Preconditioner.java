package optim.nonlinear.scalar.gradient;

public interface Preconditioner {
    /**
     * Precondition a search direction.
     * <p>
     * The returned preconditioned search direction must be computed fast or
     * the algorithm performances will drop drastically. A classical approach
     * is to compute only the diagonal elements of the hessian and to divide
     * the raw search direction by these elements if they are all positive.
     * If at least one of them is negative, it is safer to return a clone of
     * the raw search direction as if the hessian was the identity matrix. The
     * rationale for this simplified choice is that a negative diagonal element
     * means the current point is far from the optimum and preconditioning will
     * not be efficient anyway in this case.
     * </p>
     * @param point current point at which the search direction was computed
     * @param r raw search direction (i.e. opposite of the gradient)
     * @return approximation of H<sup>-1</sup>r where H is the objective function hessian
     */
    double[] precondition(double[] point, double[] r);
}
