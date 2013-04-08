package stat.correlation;

import exception.NumberIsTooSmallException;
import exception.util.LocalizedFormats;


class StorelessBivariateCovariance {

    /** the mean of variable x */
    private double meanX;

    /** the mean of variable y */
    private double meanY;

    /** number of observations */
    private double n;

    /** the running covariance estimate */
    private double covarianceNumerator;

    /** flag for bias correction */
    private boolean biasCorrected;

    /**
     * Create an empty {@link StorelessBivariateCovariance} instance with
     * bias correction.
     */
    public StorelessBivariateCovariance() {
        this(true);
    }

    /**
     * Create an empty {@link StorelessBivariateCovariance} instance.
     *
     * @param biasCorrection if <code>true</code> the covariance estimate is corrected
     * for bias, i.e. n-1 in the denominator, otherwise there is no bias correction,
     * i.e. n in the denominator.
     */
    public StorelessBivariateCovariance(final boolean biasCorrection) {
        meanX = meanY = 0.0;
        n = 0;
        covarianceNumerator = 0.0;
        biasCorrected = biasCorrection;
    }

    /**
     * Update the covariance estimation with a pair of variables (x, y).
     *
     * @param x the x value
     * @param y the y value
     */
    public void increment(final double x, final double y) {
        n++;
        final double deltaX = x - meanX;
        final double deltaY = y - meanY;
        meanX += deltaX / n;
        meanY += deltaY / n;
        covarianceNumerator += ((n - 1.0) / n) * deltaX * deltaY;
    }

    /**
     * Returns the number of observations.
     *
     * @return number of observations
     */
    public double getN() {
        return n;
    }

    /**
     * Return the current covariance estimate.
     *
     * @return the current covariance
     * @throws NumberIsTooSmallException if the number of observations
     * is &lt; 2
     */
    public double getResult() throws NumberIsTooSmallException {
        if (n < 2) {
            throw new NumberIsTooSmallException(LocalizedFormats.INSUFFICIENT_DIMENSION,
                                                n, 2, true);
        }
        if (biasCorrected) {
            return covarianceNumerator / (n - 1d);
        } else {
            return covarianceNumerator / n;
        }
    }
}

