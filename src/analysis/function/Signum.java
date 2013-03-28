package analysis.function;

import math.util.FastMath;
import analysis.UnivariateFunction;


public class Signum implements UnivariateFunction {
    /** {@inheritDoc} */
    public double value(double x) {
        return FastMath.signum(x);
    }
}
