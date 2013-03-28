package linear;

import exception.NumberIsTooSmallException;
import exception.util.ExceptionContext;
import exception.util.LocalizedFormats;

public class NonPositiveDefiniteMatrixException extends NumberIsTooSmallException {
    /** Serializable version Id. */
    private static final long serialVersionUID = 1641613838113738061L;
    /** Index (diagonal element). */
    private final int index;
    /** Threshold. */
    private final double threshold;

    /**
     * Construct an exception.
     *
     * @param wrong Value that fails the positivity check.
     * @param index Row (and column) index.
     * @param threshold Absolute positivity threshold.
     */
    public NonPositiveDefiniteMatrixException(double wrong,
                                              int index,
                                              double threshold) {
        super(wrong, threshold, false);
        this.index = index;
        this.threshold = threshold;

        final ExceptionContext context = getContext();
        context.addMessage(LocalizedFormats.NOT_POSITIVE_DEFINITE_MATRIX);
        context.addMessage(LocalizedFormats.ARRAY_ELEMENT, wrong, index);
    }

    /**
     * @return the row index.
     */
    public int getRow() {
        return index;
    }
    /**
     * @return the column index.
     */
    public int getColumn() {
        return index;
    }
    /**
     * @return the absolute positivity threshold.
     */
    public double getThreshold() {
        return threshold;
    }
}
