package math.util;

import java.io.Serializable;

import exception.MathIllegalArgumentException;
import exception.NullArgumentException;
import exception.util.LocalizedFormats;

public class DefaultTransformer implements NumberTransformer, Serializable {

    /** Serializable version identifier */
    private static final long serialVersionUID = 4019938025047800455L;

    /**
     * @param o  the object that gets transformed.
     * @return a double primitive representation of the Object o.
     * @throws NullArgumentException if Object <code>o</code> is {@code null}.
     * @throws MathIllegalArgumentException if Object <code>o</code>
     * cannot successfully be transformed
     * @see <a href="http://commons.apache.org/collections/api-release/org/apache/commons/collections/Transformer.html">Commons Collections Transformer</a>
     */
    public double transform(Object o)
        throws NullArgumentException, MathIllegalArgumentException {

        if (o == null) {
            throw new NullArgumentException(LocalizedFormats.OBJECT_TRANSFORMATION);
        }

        if (o instanceof Number) {
            return ((Number)o).doubleValue();
        }

        try {
            return Double.valueOf(o.toString()).doubleValue();
        } catch (NumberFormatException e) {
            throw new MathIllegalArgumentException(LocalizedFormats.CANNOT_TRANSFORM_TO_DOUBLE,
                                                   o.toString());
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return other instanceof DefaultTransformer;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        // some arbitrary number ...
        return 401993047;
    }

}
