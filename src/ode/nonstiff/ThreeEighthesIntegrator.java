package ode.nonstiff;


public class ThreeEighthesIntegrator extends RungeKuttaIntegrator {

  /** Time steps Butcher array. */
  private static final double[] STATIC_C = {
    1.0 / 3.0, 2.0 / 3.0, 1.0
  };

  /** Internal weights Butcher array. */
  private static final double[][] STATIC_A = {
    {  1.0 / 3.0 },
    { -1.0 / 3.0, 1.0 },
    {  1.0, -1.0, 1.0 }
  };

  /** Propagation weights Butcher array. */
  private static final double[] STATIC_B = {
    1.0 / 8.0, 3.0 / 8.0, 3.0 / 8.0, 1.0 / 8.0
  };

  /** Simple constructor.
   * Build a 3/8 integrator with the given step.
   * @param step integration step
   */
  public ThreeEighthesIntegrator(final double step) {
    super("3/8", STATIC_C, STATIC_A, STATIC_B, new ThreeEighthesStepInterpolator(), step);
  }

}
