package stat.clustering;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import math.util.MathArrays;

public class EuclideanIntegerPoint implements Clusterable<EuclideanIntegerPoint>, Serializable {

    /** Serializable version identifier. */
    private static final long serialVersionUID = 3946024775784901369L;

    /** Point coordinates. */
    private final int[] point;

    /**
     * Build an instance wrapping an integer array.
     * <p>The wrapped array is referenced, it is <em>not</em> copied.</p>
     * @param point the n-dimensional point in integer space
     */
    public EuclideanIntegerPoint(final int[] point) {
        this.point = point;
    }

    /**
     * Get the n-dimensional point in integer space.
     * @return a reference (not a copy!) to the wrapped array
     */
    public int[] getPoint() {
        return point;
    }

    /** {@inheritDoc} */
    public double distanceFrom(final EuclideanIntegerPoint p) {
        return MathArrays.distance(point, p.getPoint());
    }

    /** {@inheritDoc} */
    public EuclideanIntegerPoint centroidOf(final Collection<EuclideanIntegerPoint> points) {
        int[] centroid = new int[getPoint().length];
        for (EuclideanIntegerPoint p : points) {
            for (int i = 0; i < centroid.length; i++) {
                centroid[i] += p.getPoint()[i];
            }
        }
        for (int i = 0; i < centroid.length; i++) {
            centroid[i] /= points.size();
        }
        return new EuclideanIntegerPoint(centroid);
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof EuclideanIntegerPoint)) {
            return false;
        }
        return Arrays.equals(point, ((EuclideanIntegerPoint) other).point);
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Arrays.hashCode(point);
    }

    /**
     * {@inheritDoc}
     * @since 2.1
     */
    @Override
    public String toString() {
        return Arrays.toString(point);
    }

}
