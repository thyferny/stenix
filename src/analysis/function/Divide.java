package analysis.function;

import analysis.BivariateFunction;

public class Divide implements BivariateFunction {
    /** {@inheritDoc} */
    public double value(double x, double y) {
        return x / y;
    }
}
