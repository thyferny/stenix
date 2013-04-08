package optim.linear;

public enum Relationship {
    /** Equality relationship. */
    EQ("="),
    /** Lesser than or equal relationship. */
    LEQ("<="),
    /** Greater than or equal relationship. */
    GEQ(">=");

    /** Display string for the relationship. */
    private final String stringValue;

    /**
     * Simple constructor.
     *
     * @param stringValue Display string for the relationship.
     */
    private Relationship(String stringValue) {
        this.stringValue = stringValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }

    /**
     * Gets the relationship obtained when multiplying all coefficients by -1.
     *
     * @return the opposite relationship.
     */
    public Relationship oppositeRelationship() {
        switch (this) {
        case LEQ :
            return GEQ;
        case GEQ :
            return LEQ;
        default :
            return EQ;
        }
    }
}
