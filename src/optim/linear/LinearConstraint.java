package optim.linear;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import linear.ArrayRealVector;
import linear.MatrixUtils;
import linear.RealVector;

public class LinearConstraint implements Serializable {
    /** Serializable version identifier. */
    private static final long serialVersionUID = -764632794033034092L;
    /** Coefficients of the constraint (left hand side). */
    private final transient RealVector coefficients;
    /** Relationship between left and right hand sides (=, &lt;=, >=). */
    private final Relationship relationship;
    /** Value of the constraint (right hand side). */
    private final double value;

    /**
     * Build a constraint involving a single linear equation.
     * <p>
     * A linear constraint with a single linear equation has one of the forms:
     * <ul>
     *   <li>c<sub>1</sub>x<sub>1</sub> + ... c<sub>n</sub>x<sub>n</sub> = v</li>
     *   <li>c<sub>1</sub>x<sub>1</sub> + ... c<sub>n</sub>x<sub>n</sub> &lt;= v</li>
     *   <li>c<sub>1</sub>x<sub>1</sub> + ... c<sub>n</sub>x<sub>n</sub> >= v</li>
     * </ul>
     * </p>
     * @param coefficients The coefficients of the constraint (left hand side)
     * @param relationship The type of (in)equality used in the constraint
     * @param value The value of the constraint (right hand side)
     */
    public LinearConstraint(final double[] coefficients,
                            final Relationship relationship,
                            final double value) {
        this(new ArrayRealVector(coefficients), relationship, value);
    }

    /**
     * Build a constraint involving a single linear equation.
     * <p>
     * A linear constraint with a single linear equation has one of the forms:
     * <ul>
     *   <li>c<sub>1</sub>x<sub>1</sub> + ... c<sub>n</sub>x<sub>n</sub> = v</li>
     *   <li>c<sub>1</sub>x<sub>1</sub> + ... c<sub>n</sub>x<sub>n</sub> &lt;= v</li>
     *   <li>c<sub>1</sub>x<sub>1</sub> + ... c<sub>n</sub>x<sub>n</sub> >= v</li>
     * </ul>
     * </p>
     * @param coefficients The coefficients of the constraint (left hand side)
     * @param relationship The type of (in)equality used in the constraint
     * @param value The value of the constraint (right hand side)
     */
    public LinearConstraint(final RealVector coefficients,
                            final Relationship relationship,
                            final double value) {
        this.coefficients = coefficients;
        this.relationship = relationship;
        this.value        = value;
    }

    /**
     * Build a constraint involving two linear equations.
     * <p>
     * A linear constraint with two linear equation has one of the forms:
     * <ul>
     *   <li>l<sub>1</sub>x<sub>1</sub> + ... l<sub>n</sub>x<sub>n</sub> + l<sub>cst</sub> =
     *       r<sub>1</sub>x<sub>1</sub> + ... r<sub>n</sub>x<sub>n</sub> + r<sub>cst</sub></li>
     *   <li>l<sub>1</sub>x<sub>1</sub> + ... l<sub>n</sub>x<sub>n</sub> + l<sub>cst</sub> &lt;=
     *       r<sub>1</sub>x<sub>1</sub> + ... r<sub>n</sub>x<sub>n</sub> + r<sub>cst</sub></li>
     *   <li>l<sub>1</sub>x<sub>1</sub> + ... l<sub>n</sub>x<sub>n</sub> + l<sub>cst</sub> >=
     *       r<sub>1</sub>x<sub>1</sub> + ... r<sub>n</sub>x<sub>n</sub> + r<sub>cst</sub></li>
     * </ul>
     * </p>
     * @param lhsCoefficients The coefficients of the linear expression on the left hand side of the constraint
     * @param lhsConstant The constant term of the linear expression on the left hand side of the constraint
     * @param relationship The type of (in)equality used in the constraint
     * @param rhsCoefficients The coefficients of the linear expression on the right hand side of the constraint
     * @param rhsConstant The constant term of the linear expression on the right hand side of the constraint
     */
    public LinearConstraint(final double[] lhsCoefficients, final double lhsConstant,
                            final Relationship relationship,
                            final double[] rhsCoefficients, final double rhsConstant) {
        double[] sub = new double[lhsCoefficients.length];
        for (int i = 0; i < sub.length; ++i) {
            sub[i] = lhsCoefficients[i] - rhsCoefficients[i];
        }
        this.coefficients = new ArrayRealVector(sub, false);
        this.relationship = relationship;
        this.value        = rhsConstant - lhsConstant;
    }

    /**
     * Build a constraint involving two linear equations.
     * <p>
     * A linear constraint with two linear equation has one of the forms:
     * <ul>
     *   <li>l<sub>1</sub>x<sub>1</sub> + ... l<sub>n</sub>x<sub>n</sub> + l<sub>cst</sub> =
     *       r<sub>1</sub>x<sub>1</sub> + ... r<sub>n</sub>x<sub>n</sub> + r<sub>cst</sub></li>
     *   <li>l<sub>1</sub>x<sub>1</sub> + ... l<sub>n</sub>x<sub>n</sub> + l<sub>cst</sub> &lt;=
     *       r<sub>1</sub>x<sub>1</sub> + ... r<sub>n</sub>x<sub>n</sub> + r<sub>cst</sub></li>
     *   <li>l<sub>1</sub>x<sub>1</sub> + ... l<sub>n</sub>x<sub>n</sub> + l<sub>cst</sub> >=
     *       r<sub>1</sub>x<sub>1</sub> + ... r<sub>n</sub>x<sub>n</sub> + r<sub>cst</sub></li>
     * </ul>
     * </p>
     * @param lhsCoefficients The coefficients of the linear expression on the left hand side of the constraint
     * @param lhsConstant The constant term of the linear expression on the left hand side of the constraint
     * @param relationship The type of (in)equality used in the constraint
     * @param rhsCoefficients The coefficients of the linear expression on the right hand side of the constraint
     * @param rhsConstant The constant term of the linear expression on the right hand side of the constraint
     */
    public LinearConstraint(final RealVector lhsCoefficients, final double lhsConstant,
                            final Relationship relationship,
                            final RealVector rhsCoefficients, final double rhsConstant) {
        this.coefficients = lhsCoefficients.subtract(rhsCoefficients);
        this.relationship = relationship;
        this.value        = rhsConstant - lhsConstant;
    }

    /**
     * Gets the coefficients of the constraint (left hand side).
     *
     * @return the coefficients of the constraint (left hand side).
     */
    public RealVector getCoefficients() {
        return coefficients;
    }

    /**
     * Gets the relationship between left and right hand sides.
     *
     * @return the relationship between left and right hand sides.
     */
    public Relationship getRelationship() {
        return relationship;
    }

    /**
     * Gets the value of the constraint (right hand side).
     *
     * @return the value of the constraint (right hand side).
     */
    public double getValue() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof LinearConstraint) {
            LinearConstraint rhs = (LinearConstraint) other;
            return relationship == rhs.relationship &&
                value == rhs.value &&
                coefficients.equals(rhs.coefficients);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return relationship.hashCode() ^
            Double.valueOf(value).hashCode() ^
            coefficients.hashCode();
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
