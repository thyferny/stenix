package stat.ranking;

public enum NaNStrategy {

    /** NaNs are considered minimal in the ordering */
    MINIMAL,

    /** NaNs are considered maximal in the ordering */
    MAXIMAL,

    /** NaNs are removed before computing ranks */
    REMOVED,

    /** NaNs are left in place */
    FIXED,

    /** NaNs result in an exception
     * @since 3.1
     */
    FAILED
}
