package ode.nonstiff;

public class ClassicalRungeKuttaIntegrator extends RungeKuttaIntegrator {

  /** Time steps Butcher array. */
  private static final double[] STATIC_C = {
    1.0 / 2.0, 1.0 / 2.0, 1.0
  };

  /** Internal weights Butcher array. */
  private static final double[][] STATIC_A = {
    { 1.0 / 2.0 },
    { 0.0, 1.0 / 2.0 },
    { 0.0, 0.0, 1.0 }
  };

  /** Propagation weights Butcher array. */
  private static final double[] STATIC_B = {
    1.0 / 6.0, 1.0 / 3.0, 1.0 / 3.0, 1.0 / 6.0
  };

  /** Simple constructor.
   * Build a fourth-order Runge-Kutta integrator with the given
   * step.
   * @param step integration step
   */
  public ClassicalRungeKuttaIntegrator(final double step) {
    super("classical Runge-Kutta", STATIC_C, STATIC_A, STATIC_B,
          new ClassicalRungeKuttaStepInterpolator(), step);
  }

}
