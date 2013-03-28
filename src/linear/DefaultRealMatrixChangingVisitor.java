package linear;


public class DefaultRealMatrixChangingVisitor implements RealMatrixChangingVisitor {
    /** {@inheritDoc} */
    public void start(int rows, int columns,
                      int startRow, int endRow, int startColumn, int endColumn) {
    }

    /** {@inheritDoc} */
    public double visit(int row, int column, double value) {
        return value;
    }

    /** {@inheritDoc} */
    public double end() {
        return 0;
    }
}
