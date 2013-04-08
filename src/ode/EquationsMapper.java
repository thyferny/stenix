package ode;

import java.io.Serializable;

import exception.DimensionMismatchException;

public class EquationsMapper implements Serializable {

    /** Serializable UID. */
    private static final long serialVersionUID = 20110925L;

    /** Index of the first equation element in complete state arrays. */
    private final int firstIndex;

    /** Dimension of the secondary state parameters. */
    private final int dimension;

    /** simple constructor.
     * @param firstIndex index of the first equation element in complete state arrays
     * @param dimension dimension of the secondary state parameters
     */
    public EquationsMapper(final int firstIndex, final int dimension) {
        this.firstIndex = firstIndex;
        this.dimension  = dimension;
    }

    /** Get the index of the first equation element in complete state arrays.
     * @return index of the first equation element in complete state arrays
     */
    public int getFirstIndex() {
        return firstIndex;
    }

    /** Get the dimension of the secondary state parameters.
     * @return dimension of the secondary state parameters
     */
    public int getDimension() {
        return dimension;
    }

    /** Extract equation data from a complete state or derivative array.
     * @param complete complete state or derivative array from which
     * equation data should be retrieved
     * @param equationData placeholder where to put equation data
     * @throws DimensionMismatchException if the dimension of the equation data does not
     * match the mapper dimension
     */
    public void extractEquationData(double[] complete, double[] equationData)
        throws DimensionMismatchException {
        if (equationData.length != dimension) {
            throw new DimensionMismatchException(equationData.length, dimension);
        }
        System.arraycopy(complete, firstIndex, equationData, 0, dimension);
    }

    /** Insert equation data into a complete state or derivative array.
     * @param equationData equation data to be inserted into the complete array
     * @param complete placeholder where to put equation data (only the
     * part corresponding to the equation will be overwritten)
     * @throws DimensionMismatchException if the dimension of the equation data does not
     * match the mapper dimension
     */
    public void insertEquationData(double[] equationData, double[] complete)
        throws DimensionMismatchException {
        if (equationData.length != dimension) {
            throw new DimensionMismatchException(equationData.length, dimension);
        }
        System.arraycopy(equationData, 0, complete, firstIndex, dimension);
    }

}
