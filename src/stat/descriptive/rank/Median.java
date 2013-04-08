package stat.descriptive.rank;

import java.io.Serializable;

import exception.NullArgumentException;


public class Median extends Percentile implements Serializable {

    /** Serializable version identifier */
    private static final long serialVersionUID = -3961477041290915687L;

    /**
     * Default constructor.
     */
    public Median() {
        // No try-catch or advertised exception - arg is valid
        super(50.0);
    }

    /**
     * Copy constructor, creates a new {@code Median} identical
     * to the {@code original}
     *
     * @param original the {@code Median} instance to copy
     * @throws NullArgumentException if original is null
     */
    public Median(Median original) throws NullArgumentException {
        super(original);
    }

}
