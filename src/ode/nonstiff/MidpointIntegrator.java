package ode.nonstiff;


public class MidpointIntegrator extends RungeKuttaIntegrator {

  /** Time steps Butcher array. */
  private static final double[] STATIC_C = {
    1.0 / 2.0
  };

  /** Internal weights Butcher array. */
  private static final double[][] STATIC_A = {
    { 1.0 / 2.0 }
  };

  /** Propagation weights Butcher array. */
  private static final double[] STATIC_B = {
    0.0, 1.0
  };

  /** Simple constructor.
   * Build a midpoint integrator with the given step.
   * @param step integration step
   */
  public MidpointIntegrator(final double step) {
    super("midpoint", STATIC_C, STATIC_A, STATIC_B, new MidpointStepInterpolator(), step);
  }

}
