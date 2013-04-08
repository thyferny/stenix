package complex;

import java.io.Serializable;

import common.Field;
import common.FieldElement;

public class ComplexField implements Field<Complex>, Serializable  {

    /** Serializable version identifier. */
    private static final long serialVersionUID = -6130362688700788798L;

    /** Private constructor for the singleton.
     */
    private ComplexField() {
    }

    /** Get the unique instance.
     * @return the unique instance
     */
    public static ComplexField getInstance() {
        return LazyHolder.INSTANCE;
    }

    /** {@inheritDoc} */
    public Complex getOne() {
        return Complex.ONE;
    }

    /** {@inheritDoc} */
    public Complex getZero() {
        return Complex.ZERO;
    }

    /** {@inheritDoc} */
    public Class<? extends FieldElement<Complex>> getRuntimeClass() {
        return Complex.class;
    }

    // CHECKSTYLE: stop HideUtilityClassConstructor
    /** Holder for the instance.
     * <p>We use here the Initialization On Demand Holder Idiom.</p>
     */
    private static class LazyHolder {
        /** Cached field instance. */
        private static final ComplexField INSTANCE = new ComplexField();
    }
    // CHECKSTYLE: resume HideUtilityClassConstructor

    /** Handle deserialization of the singleton.
     * @return the singleton instance
     */
    private Object readResolve() {
        // return the singleton instance
        return LazyHolder.INSTANCE;
    }

}
