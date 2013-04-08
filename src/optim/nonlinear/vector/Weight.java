package optim.nonlinear.vector;

import linear.DiagonalMatrix;
import linear.NonSquareMatrixException;
import linear.RealMatrix;
import optim.OptimizationData;

public class Weight implements OptimizationData {
    /** Weight matrix. */
    private final RealMatrix weightMatrix;

    /**
     * Creates a diagonal weight matrix.
     *
     * @param weight List of the values of the diagonal.
     */
    public Weight(double[] weight) {
        weightMatrix = new DiagonalMatrix(weight);
    }

    /**
     * @param weight Weight matrix.
     * @throws NonSquareMatrixException if the argument is not
     * a square matrix.
     */
    public Weight(RealMatrix weight) {
        if (weight.getColumnDimension() != weight.getRowDimension()) {
            throw new NonSquareMatrixException(weight.getColumnDimension(),
                                               weight.getRowDimension());
        }

        weightMatrix = weight.copy();
    }

    /**
     * Gets the initial guess.
     *
     * @return the initial guess.
     */
    public RealMatrix getWeight() {
        return weightMatrix.copy();
    }
}
