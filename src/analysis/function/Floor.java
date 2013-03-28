package analysis.function;

import math.util.FastMath;
import analysis.UnivariateFunction;

public class Floor implements UnivariateFunction {
    /** {@inheritDoc} */
    public double value(double x) {
        return FastMath.floor(x);
    }
}
