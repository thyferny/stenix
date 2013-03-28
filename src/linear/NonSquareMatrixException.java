package linear;

import exception.DimensionMismatchException;
import exception.util.LocalizedFormats;

public class NonSquareMatrixException extends DimensionMismatchException {
    /** Serializable version Id. */
    private static final long serialVersionUID = -660069396594485772L;

    /**
     * Construct an exception from the mismatched dimensions.
     *
     * @param wrong Row dimension.
     * @param expected Column dimension.
     */
    public NonSquareMatrixException(int wrong,
                                    int expected) {
        super(LocalizedFormats.NON_SQUARE_MATRIX, wrong, expected);
    }
}
