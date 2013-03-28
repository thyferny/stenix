package linear;

import math.util.MathArrays;
import analysis.function.Sqrt;

public class JacobiPreconditioner extends RealLinearOperator {

    /** The diagonal coefficients of the preconditioner. */
    private final ArrayRealVector diag;

    /**
     * Creates a new instance of this class.
     *
     * @param diag the diagonal coefficients of the linear operator to be
     * preconditioned
     * @param deep {@code true} if a deep copy of the above array should be
     * performed
     */
    public JacobiPreconditioner(final double[] diag, final boolean deep) {
        this.diag = new ArrayRealVector(diag, deep);
    }

    /**
     * Creates a new instance of this class. This method extracts the diagonal
     * coefficients of the specified linear operator. If {@code a} does not
     * extend {@link AbstractRealMatrix}, then the coefficients of the
     * underlying matrix are not accessible, coefficient extraction is made by
     * matrix-vector products with the basis vectors (and might therefore take
     * some time). With matrices, direct entry access is carried out.
     *
     * @param a the linear operator for which the preconditioner should be built
     * @return the diagonal preconditioner made of the inverse of the diagonal
     * coefficients of the specified linear operator
     * @throws NonSquareOperatorException if {@code a} is not square
     */
    public static JacobiPreconditioner create(final RealLinearOperator a)
        throws NonSquareOperatorException {
        final int n = a.getColumnDimension();
        if (a.getRowDimension() != n) {
            throw new NonSquareOperatorException(a.getRowDimension(), n);
        }
        final double[] diag = new double[n];
        if (a instanceof AbstractRealMatrix) {
            final AbstractRealMatrix m = (AbstractRealMatrix) a;
            for (int i = 0; i < n; i++) {
                diag[i] = m.getEntry(i, i);
            }
        } else {
            final ArrayRealVector x = new ArrayRealVector(n);
            for (int i = 0; i < n; i++) {
                x.set(0.);
                x.setEntry(i, 1.);
                diag[i] = a.operate(x).getEntry(i);
            }
        }
        return new JacobiPreconditioner(diag, false);
    }

    /** {@inheritDoc} */
    @Override
    public int getColumnDimension() {
        return diag.getDimension();
    }

    /** {@inheritDoc} */
    @Override
    public int getRowDimension() {
        return diag.getDimension();
    }

    /** {@inheritDoc} */
    @Override
    public RealVector operate(final RealVector x) {
        // Dimension check is carried out by ebeDivide
        return new ArrayRealVector(MathArrays.ebeDivide(x.toArray(),
                                                        diag.toArray()),
                                   false);
    }

    /**
     * Returns the square root of {@code this} diagonal operator. More
     * precisely, this method returns
     * P = diag(1 / &radic;A<sub>11</sub>, 1 / &radic;A<sub>22</sub>, &hellip;).
     *
     * @return the square root of {@code this} preconditioner
     * @since 3.1
     */
    public RealLinearOperator sqrt() {
        final RealVector sqrtDiag = diag.map(new Sqrt());
        return new RealLinearOperator() {
            /** {@inheritDoc} */
            @Override
            public RealVector operate(final RealVector x) {
                return new ArrayRealVector(MathArrays.ebeDivide(x.toArray(),
                                                                sqrtDiag.toArray()),
                                           false);
            }

            /** {@inheritDoc} */
            @Override
            public int getRowDimension() {
                return sqrtDiag.getDimension();
            }

            /** {@inheritDoc} */
            @Override
            public int getColumnDimension() {
                return sqrtDiag.getDimension();
            }
        };
    }
}
