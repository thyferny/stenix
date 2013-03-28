package linear;

import common.FieldElement;

public interface FieldDecompositionSolver<T extends FieldElement<T>> {

    /** Solve the linear equation A &times; X = B for matrices A.
     * <p>The A matrix is implicit, it is provided by the underlying
     * decomposition algorithm.</p>
     * @param b right-hand side of the equation A &times; X = B
     * @return a vector X that minimizes the two norm of A &times; X - B
     * @throws org.apache.commons.math3.exception.DimensionMismatchException
     * if the matrices dimensions do not match.
     * @throws SingularMatrixException
     * if the decomposed matrix is singular.
     */
    FieldVector<T> solve(final FieldVector<T> b);

    /** Solve the linear equation A &times; X = B for matrices A.
     * <p>The A matrix is implicit, it is provided by the underlying
     * decomposition algorithm.</p>
     * @param b right-hand side of the equation A &times; X = B
     * @return a matrix X that minimizes the two norm of A &times; X - B
     * @throws org.apache.commons.math3.exception.DimensionMismatchException
     * if the matrices dimensions do not match.
     * @throws SingularMatrixException
     * if the decomposed matrix is singular.
     */
    FieldMatrix<T> solve(final FieldMatrix<T> b);

    /**
     * Check if the decomposed matrix is non-singular.
     * @return true if the decomposed matrix is non-singular
     */
    boolean isNonSingular();

    /** Get the inverse (or pseudo-inverse) of the decomposed matrix.
     * @return inverse matrix
     * @throws SingularMatrixException
     * if the decomposed matrix is singular.
     */
    FieldMatrix<T> getInverse();
}
