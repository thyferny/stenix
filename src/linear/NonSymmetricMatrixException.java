package linear;

import exception.MathIllegalArgumentException;
import exception.util.LocalizedFormats;

public class NonSymmetricMatrixException extends MathIllegalArgumentException {
    /** Serializable version Id. */
    private static final long serialVersionUID = -7518495577824189882L;
    /** Row. */
    private final int row;
    /** Column. */
    private final int column;
    /** Threshold. */
    private final double threshold;

    /**
     * Construct an exception.
     *
     * @param row Row index.
     * @param column Column index.
     * @param threshold Relative symmetry threshold.
     */
    public NonSymmetricMatrixException(int row,
                                       int column,
                                       double threshold) {
        super(LocalizedFormats.NON_SYMMETRIC_MATRIX, row, column, threshold);
        this.row = row;
        this.column = column;
        this.threshold = threshold;
    }

    /**
     * @return the row index of the entry.
     */
    public int getRow() {
        return row;
    }
    /**
     * @return the column index of the entry.
     */
    public int getColumn() {
        return column;
    }
    /**
     * @return the relative symmetry threshold.
     */
    public double getThreshold() {
        return threshold;
    }
}
