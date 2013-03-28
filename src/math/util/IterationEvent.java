package math.util;

import java.util.EventObject;


public class IterationEvent extends EventObject {
    /** */
    private static final long serialVersionUID = 20120128L;

    /** The number of iterations performed so far. */
    private final int iterations;

    /**
     * Creates a new instance of this class.
     *
     * @param source the iterative algorithm on which the event initially
     * occurred
     * @param iterations the number of iterations performed at the time
     * {@code this} event is created
     */
    public IterationEvent(final Object source, final int iterations) {
        super(source);
        this.iterations = iterations;
    }

    /**
     * Returns the number of iterations performed at the time {@code this} event
     * is created.
     *
     * @return the number of iterations performed
     */
    public int getIterations() {
        return iterations;
    }
 }
