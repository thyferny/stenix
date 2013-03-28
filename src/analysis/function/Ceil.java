package analysis.function;

import math.util.FastMath;
import analysis.UnivariateFunction;


public class Ceil implements UnivariateFunction {
    /** {@inheritDoc} */
    public double value(double x) {
        return FastMath.ceil(x);
    }
}
