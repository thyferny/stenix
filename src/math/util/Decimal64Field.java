package math.util;

import common.Field;
import common.FieldElement;


public class Decimal64Field implements Field<Decimal64> {

    /** The unique instance of this class. */
    private static final Decimal64Field INSTANCE = new Decimal64Field();

    /** Default constructor. */
    private Decimal64Field() {
        // Do nothing
    }

    /**
     * Returns the unique instance of this class.
     *
     * @return the unique instance of this class
     */
    public static final Decimal64Field getInstance() {
        return INSTANCE;
    }

    /** {@inheritDoc} */
    public Decimal64 getZero() {
        return Decimal64.ZERO;
    }

    /** {@inheritDoc} */
    public Decimal64 getOne() {
        return Decimal64.ONE;
    }

    /** {@inheritDoc} */
    public Class<? extends FieldElement<Decimal64>> getRuntimeClass() {
        return Decimal64.class;
    }
}
