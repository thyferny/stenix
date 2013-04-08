package optim.linear;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import linear.ArrayRealVector;
import linear.MatrixUtils;
import linear.RealVector;
import optim.OptimizationData;
import analysis.MultivariateFunction;

public class LinearObjectiveFunction
    implements MultivariateFunction,
               OptimizationData,
               Serializable {
    /** Serializable version identifier. */
    private static final long serialVersionUID = -4531815507568396090L;
    /** Coefficients of the linear equation (c<sub>i</sub>). */
    private final transient RealVector coefficients;
    /** Constant term of the linear equation. */
    private final double constantTerm;

    /**
     * @param coefficients Coefficients for the linear equation being optimized.
     * @param constantTerm Constant term of the linear equation.
     */
    public LinearObjectiveFunction(double[] coefficients, double constantTerm) {
        this(new ArrayRealVector(coefficients), constantTerm);
    }

    /**
     * @param coefficients Coefficients for the linear equation being optimized.
     * @param constantTerm Constant term of the linear equation.
     */
    public LinearObjectiveFunction(RealVector coefficients, double constantTerm) {
        this.coefficients = coefficients;
        this.constantTerm = constantTerm;
    }

    /**
     * Gets the coefficients of the linear equation being optimized.
     *
     * @return coefficients of the linear equation being optimized.
     */
    public RealVector getCoefficients() {
        return coefficients;
    }

    /**
     * Gets the constant of the linear equation being optimized.
     *
     * @return constant of the linear equation being optimized.
     */
    public double getConstantTerm() {
        return constantTerm;
    }

    /**
     * Computes the value of the linear equation at the current point.
     *
     * @param point Point at which linear equation must be evaluated.
     * @return the value of the linear equation at the current point.
     */
    public double value(final double[] point) {
        return value(new ArrayRealVector(point, false));
    }

    /**
     * Computes the value of the linear equation at the current point.
     *
     * @param point Point at which linear equation must be evaluated.
     * @return the value of the linear equation at the current point.
     */
    public double value(final RealVector point) {
        return coefficients.dotProduct(point) + constantTerm;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof LinearObjectiveFunction) {
            LinearObjectiveFunction rhs = (LinearObjectiveFunction) other;
          return (constantTerm == rhs.constantTerm) && coefficients.equals(rhs.coefficients);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Double.valueOf(constantTerm).hashCode() ^ coefficients.hashCode();
    }

    /**
     * Serialize the instance.
     * @param oos stream where object should be written
     * @throws IOException if object cannot be written to stream
     */
    private void writeObject(ObjectOutputStream oos)
        throws IOException {
        oos.defaultWriteObject();
        MatrixUtils.serializeRealVector(coefficients, oos);
    }

    /**
     * Deserialize the instance.
     * @param ois stream from which the object should be read
     * @throws ClassNotFoundException if a class in the stream cannot be found
     * @throws IOException if object cannot be read from the stream
     */
    private void readObject(ObjectInputStream ois)
      throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        MatrixUtils.deserializeRealVector(this, "coefficients", ois);
    }
}
