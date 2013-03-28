package analysis.function;

import math.util.FastMath;
import analysis.BivariateFunction;

public class Atan2 implements BivariateFunction {
    /** {@inheritDoc} */
    public double value(double x, double y) {
        return FastMath.atan2(x, y);
    }
}
