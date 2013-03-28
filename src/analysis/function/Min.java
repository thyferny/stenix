package analysis.function;

import math.util.FastMath;
import analysis.BivariateFunction;

public class Min implements BivariateFunction {
    /** {@inheritDoc} */
    public double value(double x, double y) {
        return FastMath.min(x, y);
    }
}
