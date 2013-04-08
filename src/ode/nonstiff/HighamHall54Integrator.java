package ode.nonstiff;

import math.util.FastMath;


public class HighamHall54Integrator extends EmbeddedRungeKuttaIntegrator {

  /** Integrator method name. */
  private static final String METHOD_NAME = "Higham-Hall 5(4)";

  /** Time steps Butcher array. */
  private static final double[] STATIC_C = {
    2.0/9.0, 1.0/3.0, 1.0/2.0, 3.0/5.0, 1.0, 1.0
  };

  /** Internal weights Butcher array. */
  private static final double[][] STATIC_A = {
    {2.0/9.0},
    {1.0/12.0, 1.0/4.0},
    {1.0/8.0, 0.0, 3.0/8.0},
    {91.0/500.0, -27.0/100.0, 78.0/125.0, 8.0/125.0},
    {-11.0/20.0, 27.0/20.0, 12.0/5.0, -36.0/5.0, 5.0},
    {1.0/12.0, 0.0, 27.0/32.0, -4.0/3.0, 125.0/96.0, 5.0/48.0}
  };

  /** Propagation weights Butcher array. */
  private static final double[] STATIC_B = {
    1.0/12.0, 0.0, 27.0/32.0, -4.0/3.0, 125.0/96.0, 5.0/48.0, 0.0
  };

  /** Error weights Butcher array. */
  private static final double[] STATIC_E = {
    -1.0/20.0, 0.0, 81.0/160.0, -6.0/5.0, 25.0/32.0, 1.0/16.0, -1.0/10.0
  };

  /** Simple constructor.
   * Build a fifth order Higham and Hall integrator with the given step bounds
   * @param minStep minimal step (sign is irrelevant, regardless of
   * integration direction, forward or backward), the last step can
   * be smaller than this
   * @param maxStep maximal step (sign is irrelevant, regardless of
   * integration direction, forward or backward), the last step can
   * be smaller than this
   * @param scalAbsoluteTolerance allowed absolute error
   * @param scalRelativeTolerance allowed relative error
   */
  public HighamHall54Integrator(final double minStep, final double maxStep,
                                final double scalAbsoluteTolerance,
                                final double scalRelativeTolerance) {
    super(METHOD_NAME, false, STATIC_C, STATIC_A, STATIC_B, new HighamHall54StepInterpolator(),
          minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
  }

  /** Simple constructor.
   * Build a fifth order Higham and Hall integrator with the given step bounds
   * @param minStep minimal step (sign is irrelevant, regardless of
   * integration direction, forward or backward), the last step can
   * be smaller than this
   * @param maxStep maximal step (sign is irrelevant, regardless of
   * integration direction, forward or backward), the last step can
   * be smaller than this
   * @param vecAbsoluteTolerance allowed absolute error
   * @param vecRelativeTolerance allowed relative error
   */
  public HighamHall54Integrator(final double minStep, final double maxStep,
                                final double[] vecAbsoluteTolerance,
                                final double[] vecRelativeTolerance) {
    super(METHOD_NAME, false, STATIC_C, STATIC_A, STATIC_B, new HighamHall54StepInterpolator(),
          minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
  }

  /** {@inheritDoc} */
  @Override
  public int getOrder() {
    return 5;
  }

  /** {@inheritDoc} */
  @Override
  protected double estimateError(final double[][] yDotK,
                                 final double[] y0, final double[] y1,
                                 final double h) {

    double error = 0;

    for (int j = 0; j < mainSetDimension; ++j) {
      double errSum = STATIC_E[0] * yDotK[0][j];
      for (int l = 1; l < STATIC_E.length; ++l) {
        errSum += STATIC_E[l] * yDotK[l][j];
      }

      final double yScale = FastMath.max(FastMath.abs(y0[j]), FastMath.abs(y1[j]));
      final double tol = (vecAbsoluteTolerance == null) ?
                         (scalAbsoluteTolerance + scalRelativeTolerance * yScale) :
                         (vecAbsoluteTolerance[j] + vecRelativeTolerance[j] * yScale);
      final double ratio  = h * errSum / tol;
      error += ratio * ratio;

    }

    return FastMath.sqrt(error / mainSetDimension);

  }

}
