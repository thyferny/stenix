package ode.nonstiff;


public class EulerIntegrator extends RungeKuttaIntegrator {

  /** Time steps Butcher array. */
  private static final double[] STATIC_C = {
  };

  /** Internal weights Butcher array. */
  private static final double[][] STATIC_A = {
  };

  /** Propagation weights Butcher array. */
  private static final double[] STATIC_B = {
    1.0
  };

  /** Simple constructor.
   * Build an Euler integrator with the given step.
   * @param step integration step
   */
  public EulerIntegrator(final double step) {
    super("Euler", STATIC_C, STATIC_A, STATIC_B, new EulerStepInterpolator(), step);
  }

}
