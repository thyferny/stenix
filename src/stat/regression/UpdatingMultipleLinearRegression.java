package stat.regression;

import exception.MathIllegalArgumentException;
import exception.NoDataException;

public interface UpdatingMultipleLinearRegression {

    /**
     * Returns true if a constant has been included false otherwise.
     *
     * @return true if constant exists, false otherwise
     */
    boolean hasIntercept();

    /**
     * Returns the number of observations added to the regression model.
     *
     * @return Number of observations
     */
    long getN();

    /**
     * Adds one observation to the regression model.
     *
     * @param x the independent variables which form the design matrix
     * @param y the dependent or response variable
     * @throws ModelSpecificationException if the length of {@code x} does not equal
     * the number of independent variables in the model
     */
    void addObservation(double[] x, double y) throws ModelSpecificationException;

    /**
     * Adds a series of observations to the regression model. The lengths of
     * x and y must be the same and x must be rectangular.
     *
     * @param x a series of observations on the independent variables
     * @param y a series of observations on the dependent variable
     * The length of x and y must be the same
     * @throws ModelSpecificationException if {@code x} is not rectangular, does not match
     * the length of {@code y} or does not contain sufficient data to estimate the model
     */
    void addObservations(double[][] x, double[] y) throws ModelSpecificationException;

    /**
     * Clears internal buffers and resets the regression model. This means all
     * data and derived values are initialized
     */
    void clear();


    /**
     * Performs a regression on data present in buffers and outputs a RegressionResults object
     * @return RegressionResults acts as a container of regression output
     * @throws ModelSpecificationException if the model is not correctly specified
     * @throws NoDataException if there is not sufficient data in the model to
     * estimate the regression parameters
     */
    RegressionResults regress() throws ModelSpecificationException, NoDataException;

    /**
     * Performs a regression on data present in buffers including only regressors
     * indexed in variablesToInclude and outputs a RegressionResults object
     * @param variablesToInclude an array of indices of regressors to include
     * @return RegressionResults acts as a container of regression output
     * @throws ModelSpecificationException if the model is not correctly specified
     * @throws MathIllegalArgumentException if the variablesToInclude array is null or zero length
     */
    RegressionResults regress(int[] variablesToInclude) throws ModelSpecificationException, MathIllegalArgumentException;
}
