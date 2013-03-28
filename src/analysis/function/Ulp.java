package analysis.function;

import math.util.FastMath;
import analysis.UnivariateFunction;

public class Ulp implements UnivariateFunction {
    /** {@inheritDoc} */
    public double value(double x) {
        return FastMath.ulp(x);
    }
}
