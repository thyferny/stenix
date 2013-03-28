package analysis.function;

import math.util.FastMath;
import analysis.UnivariateFunction;

public class Abs implements UnivariateFunction {
    /** {@inheritDoc} */
    public double value(double x) {
        return FastMath.abs(x);
    }
}
