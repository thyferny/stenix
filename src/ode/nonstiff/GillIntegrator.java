package ode.nonstiff;

import math.util.FastMath;

public class GillIntegrator extends RungeKuttaIntegrator {

  /** Time steps Butcher array. */
  private static final double[] STATIC_C = {
    1.0 / 2.0, 1.0 / 2.0, 1.0
  };

  /** Internal weights Butcher array. */
  private static final double[][] STATIC_A = {
    { 1.0 / 2.0 },
    { (FastMath.sqrt(2.0) - 1.0) / 2.0, (2.0 - FastMath.sqrt(2.0)) / 2.0 },
    { 0.0, -FastMath.sqrt(2.0) / 2.0, (2.0 + FastMath.sqrt(2.0)) / 2.0 }
  };

  /** Propagation weights Butcher array. */
  private static final double[] STATIC_B = {
    1.0 / 6.0, (2.0 - FastMath.sqrt(2.0)) / 6.0, (2.0 + FastMath.sqrt(2.0)) / 6.0, 1.0 / 6.0
  };

  /** Simple constructor.
   * Build a fourth-order Gill integrator with the given step.
   * @param step integration step
   */
  public GillIntegrator(final double step) {
    super("Gill", STATIC_C, STATIC_A, STATIC_B, new GillStepInterpolator(), step);
  }

}
