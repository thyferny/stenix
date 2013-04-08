package stat.ranking;

public enum TiesStrategy {

    /** Ties assigned sequential ranks in order of occurrence */
    SEQUENTIAL,

    /** Ties get the minimum applicable rank */
    MINIMUM,

    /** Ties get the maximum applicable rank */
    MAXIMUM,

    /** Ties get the average of applicable ranks */
    AVERAGE,

    /** Ties get a random integral value from among applicable ranks */
    RANDOM
}
