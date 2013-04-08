package stat.descriptive.moment;

import java.io.Serializable;

import math.util.MathUtils;
import stat.descriptive.AbstractUnivariateStatistic;
import exception.MathIllegalArgumentException;
import exception.NullArgumentException;


public class SemiVariance extends AbstractUnivariateStatistic implements Serializable {

    /**
     * The UPSIDE Direction is used to specify that the observations above the
     * cutoff point will be used to calculate SemiVariance.
     */
    public static final Direction UPSIDE_VARIANCE = Direction.UPSIDE;

    /**
     * The DOWNSIDE Direction is used to specify that the observations below
     * the cutoff point will be used to calculate SemiVariance
     */
    public static final Direction DOWNSIDE_VARIANCE = Direction.DOWNSIDE;

    /** Serializable version identifier */
    private static final long serialVersionUID = -2653430366886024994L;

    /**
     * Determines whether or not bias correction is applied when computing the
     * value of the statisic.  True means that bias is corrected.
     */
    private boolean biasCorrected = true;

    /**
     * Determines whether to calculate downside or upside SemiVariance.
     */
    private Direction varianceDirection = Direction.DOWNSIDE;

    /**
     * Constructs a SemiVariance with default (true) <code>biasCorrected</code>
     * property and default (Downside) <code>varianceDirection</code> property.
     */
    public SemiVariance() {
    }

    /**
     * Constructs a SemiVariance with the specified <code>biasCorrected</code>
     * property and default (Downside) <code>varianceDirection</code> property.
     *
     * @param biasCorrected  setting for bias correction - true means
     * bias will be corrected and is equivalent to using the argumentless
     * constructor
     */
    public SemiVariance(final boolean biasCorrected) {
        this.biasCorrected = biasCorrected;
    }


    /**
     * Constructs a SemiVariance with the specified <code>Direction</code> property
     * and default (true) <code>biasCorrected</code> property
     *
     * @param direction  setting for the direction of the SemiVariance
     * to calculate
     */
    public SemiVariance(final Direction direction) {
        this.varianceDirection = direction;
    }


    /**
     * Constructs a SemiVariance with the specified <code>isBiasCorrected</code>
     * property and the specified <code>Direction</code> property.
     *
     * @param corrected  setting for bias correction - true means
     * bias will be corrected and is equivalent to using the argumentless
     * constructor
     *
     * @param direction  setting for the direction of the SemiVariance
     * to calculate
     */
    public SemiVariance(final boolean corrected, final Direction direction) {
        this.biasCorrected = corrected;
        this.varianceDirection = direction;
    }


    /**
     * Copy constructor, creates a new {@code SemiVariance} identical
     * to the {@code original}
     *
     * @param original the {@code SemiVariance} instance to copy
     * @throws NullArgumentException  if original is null
     */
    public SemiVariance(final SemiVariance original) throws NullArgumentException {
        copy(original, this);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public SemiVariance copy() {
        SemiVariance result = new SemiVariance();
        // No try-catch or advertised exception because args are guaranteed non-null
        copy(this, result);
        return result;
    }


    /**
     * Copies source to dest.
     * <p>Neither source nor dest can be null.</p>
     *
     * @param source SemiVariance to copy
     * @param dest SemiVariance to copy to
     * @throws NullArgumentException if either source or dest is null
     */
    public static void copy(final SemiVariance source, SemiVariance dest)
        throws NullArgumentException {
        MathUtils.checkNotNull(source);
        MathUtils.checkNotNull(dest);
        dest.setData(source.getDataRef());
        dest.biasCorrected = source.biasCorrected;
        dest.varianceDirection = source.varianceDirection;
    }

    /**
      * <p>Returns the {@link SemiVariance} of the designated values against the mean, using
      * instance properties varianceDirection and biasCorrection.</p>
      *
      * <p>Returns <code>NaN</code> if the array is empty and throws
      * <code>IllegalArgumentException</code> if the array is null.</p>
      *
      * @param values the input array
      * @param start index of the first array element to include
      * @param length the number of elements to include
      * @return the SemiVariance
      * @throws MathIllegalArgumentException if the parameters are not valid
      *
      */
      @Override
      public double evaluate(final double[] values, final int start, final int length)
      throws MathIllegalArgumentException {
        double m = (new Mean()).evaluate(values, start, length);
        return evaluate(values, m, varianceDirection, biasCorrected, 0, values.length);
      }


      /**
       * This method calculates {@link SemiVariance} for the entire array against the mean, using
       * the current value of the biasCorrection instance property.
       *
       * @param values the input array
       * @param direction the {@link Direction} of the semivariance
       * @return the SemiVariance
       * @throws MathIllegalArgumentException if values is null
       *
       */
      public double evaluate(final double[] values, Direction direction)
      throws MathIllegalArgumentException {
          double m = (new Mean()).evaluate(values);
          return evaluate (values, m, direction, biasCorrected, 0, values.length);
      }

      /**
       * <p>Returns the {@link SemiVariance} of the designated values against the cutoff, using
       * instance properties variancDirection and biasCorrection.</p>
       *
       * <p>Returns <code>NaN</code> if the array is empty and throws
       * <code>MathIllegalArgumentException</code> if the array is null.</p>
       *
       * @param values the input array
       * @param cutoff the reference point
       * @return the SemiVariance
       * @throws MathIllegalArgumentException if values is null
       */
      public double evaluate(final double[] values, final double cutoff)
      throws MathIllegalArgumentException {
          return evaluate(values, cutoff, varianceDirection, biasCorrected, 0, values.length);
      }

      /**
       * <p>Returns the {@link SemiVariance} of the designated values against the cutoff in the
       * given direction, using the current value of the biasCorrection instance property.</p>
       *
       * <p>Returns <code>NaN</code> if the array is empty and throws
       * <code>MathIllegalArgumentException</code> if the array is null.</p>
       *
       * @param values the input array
       * @param cutoff the reference point
       * @param direction the {@link Direction} of the semivariance
       * @return the SemiVariance
       * @throws MathIllegalArgumentException if values is null
       */
      public double evaluate(final double[] values, final double cutoff, final Direction direction)
      throws MathIllegalArgumentException {
          return evaluate(values, cutoff, direction, biasCorrected, 0, values.length);
      }


     /**
      * <p>Returns the {@link SemiVariance} of the designated values against the cutoff
      * in the given direction with the provided bias correction.</p>
      *
      * <p>Returns <code>NaN</code> if the array is empty and throws
      * <code>IllegalArgumentException</code> if the array is null.</p>
      *
      * @param values the input array
      * @param cutoff the reference point
      * @param direction the {@link Direction} of the semivariance
      * @param corrected the BiasCorrection flag
      * @param start index of the first array element to include
      * @param length the number of elements to include
      * @return the SemiVariance
      * @throws MathIllegalArgumentException if the parameters are not valid
      *
      */
    public double evaluate (final double[] values, final double cutoff, final Direction direction,
            final boolean corrected, final int start, final int length) throws MathIllegalArgumentException {

        test(values, start, length);
        if (values.length == 0) {
            return Double.NaN;
        } else {
            if (values.length == 1) {
                return 0.0;
            } else {
                final boolean booleanDirection = direction.getDirection();

                double dev = 0.0;
                double sumsq = 0.0;
                for (int i = start; i < length; i++) {
                    if ((values[i] > cutoff) == booleanDirection) {
                       dev = values[i] - cutoff;
                       sumsq += dev * dev;
                    }
                }

                if (corrected) {
                    return sumsq / (length - 1.0);
                } else {
                    return sumsq / length;
                }
            }
        }
    }

    /**
     * Returns true iff biasCorrected property is set to true.
     *
     * @return the value of biasCorrected.
     */
    public boolean isBiasCorrected() {
        return biasCorrected;
    }

    /**
     * Sets the biasCorrected property.
     *
     * @param biasCorrected new biasCorrected property value
     */
    public void setBiasCorrected(boolean biasCorrected) {
        this.biasCorrected = biasCorrected;
    }

    /**
     * Returns the varianceDirection property.
     *
     * @return the varianceDirection
     */
    public Direction getVarianceDirection () {
        return varianceDirection;
    }

    /**
     * Sets the variance direction
     *
     * @param varianceDirection the direction of the semivariance
     */
    public void setVarianceDirection(Direction varianceDirection) {
        this.varianceDirection = varianceDirection;
    }

    /**
     * The direction of the semivariance - either upside or downside. The direction
     * is represented by boolean, with true corresponding to UPSIDE semivariance.
     */
    public enum Direction {
        /**
         * The UPSIDE Direction is used to specify that the observations above the
         * cutoff point will be used to calculate SemiVariance
         */
        UPSIDE (true),

        /**
         * The DOWNSIDE Direction is used to specify that the observations below
         * the cutoff point will be used to calculate SemiVariance
         */
        DOWNSIDE (false);

        /**
         *   boolean value  UPSIDE <-> true
         */
        private boolean direction;

        /**
         * Create a Direction with the given value.
         *
         * @param b boolean value representing the Direction. True corresponds to UPSIDE.
         */
        Direction (boolean b) {
            direction = b;
        }

        /**
         * Returns the value of this Direction. True corresponds to UPSIDE.
         *
         * @return true if direction is UPSIDE; false otherwise
         */
        boolean getDirection () {
            return direction;
        }
    }
}
