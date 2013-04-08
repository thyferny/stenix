package ode;

public class FirstOrderConverter implements FirstOrderDifferentialEquations {

    /** Underlying second order equations set. */
    private final SecondOrderDifferentialEquations equations;

    /** second order problem dimension. */
    private final int dimension;

    /** state vector. */
    private final double[] z;

    /** first time derivative of the state vector. */
    private final double[] zDot;

    /** second time derivative of the state vector. */
    private final double[] zDDot;

  /** Simple constructor.
   * Build a converter around a second order equations set.
   * @param equations second order equations set to convert
   */
  public FirstOrderConverter (final SecondOrderDifferentialEquations equations) {
      this.equations = equations;
      dimension      = equations.getDimension();
      z              = new double[dimension];
      zDot           = new double[dimension];
      zDDot          = new double[dimension];
  }

  /** Get the dimension of the problem.
   * <p>The dimension of the first order problem is twice the
   * dimension of the underlying second order problem.</p>
   * @return dimension of the problem
   */
  public int getDimension() {
    return 2 * dimension;
  }

  /** Get the current time derivative of the state vector.
   * @param t current value of the independent <I>time</I> variable
   * @param y array containing the current value of the state vector
   * @param yDot placeholder array where to put the time derivative of the state vector
   */
  public void computeDerivatives(final double t, final double[] y, final double[] yDot) {

    // split the state vector in two
    System.arraycopy(y, 0,         z,    0, dimension);
    System.arraycopy(y, dimension, zDot, 0, dimension);

    // apply the underlying equations set
    equations.computeSecondDerivatives(t, z, zDot, zDDot);

    // build the result state derivative
    System.arraycopy(zDot,  0, yDot, 0,         dimension);
    System.arraycopy(zDDot, 0, yDot, dimension, dimension);

  }

}
