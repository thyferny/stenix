package analysis.function;

import math.util.FastMath;
import analysis.BivariateFunction;


public class Max implements BivariateFunction {
    /** {@inheritDoc} */
    public double value(double x, double y) {
        return FastMath.max(x, y);
    }
}
