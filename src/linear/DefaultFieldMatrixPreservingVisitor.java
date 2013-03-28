package linear;

import common.FieldElement;

public class DefaultFieldMatrixPreservingVisitor<T extends FieldElement<T>>
    implements FieldMatrixPreservingVisitor<T> {
    /** Zero element of the field. */
    private final T zero;

    /** Build a new instance.
     * @param zero additive identity of the field
     */
    public DefaultFieldMatrixPreservingVisitor(final T zero) {
        this.zero = zero;
    }

    /** {@inheritDoc} */
    public void start(int rows, int columns,
                      int startRow, int endRow, int startColumn, int endColumn) {
    }

    /** {@inheritDoc} */
    public void visit(int row, int column, T value) {}

    /** {@inheritDoc} */
    public T end() {
        return zero;
    }
}
