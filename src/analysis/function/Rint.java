package analysis.function;

import math.util.FastMath;
import analysis.UnivariateFunction;


public class Rint implements UnivariateFunction {
    /** {@inheritDoc} */
    public double value(double x) {
        return FastMath.rint(x);
    }
}
