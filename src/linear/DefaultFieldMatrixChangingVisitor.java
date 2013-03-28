package linear;

import common.FieldElement;

public class DefaultFieldMatrixChangingVisitor<T extends FieldElement<T>>
    implements FieldMatrixChangingVisitor<T> {
    /** Zero element of the field. */
    private final T zero;

    /** Build a new instance.
     * @param zero additive identity of the field
     */
    public DefaultFieldMatrixChangingVisitor(final T zero) {
        this.zero = zero;
    }

    /** {@inheritDoc} */
    public void start(int rows, int columns,
                      int startRow, int endRow, int startColumn, int endColumn) {
    }

    /** {@inheritDoc} */
    public T visit(int row, int column, T value) {
        return value;
    }

    /** {@inheritDoc} */
    public T end() {
        return zero;
    }
}
