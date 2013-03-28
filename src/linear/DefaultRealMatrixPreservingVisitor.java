package linear;


public class DefaultRealMatrixPreservingVisitor implements RealMatrixPreservingVisitor {
    /** {@inheritDoc} */
    public void start(int rows, int columns,
                      int startRow, int endRow, int startColumn, int endColumn) {
    }

    /** {@inheritDoc} */
    public void visit(int row, int column, double value) {}

    /** {@inheritDoc} */
    public double end() {
        return 0;
    }
}
