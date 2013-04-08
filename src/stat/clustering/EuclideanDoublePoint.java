package stat.clustering;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import math.util.MathArrays;

public class EuclideanDoublePoint implements Clusterable<EuclideanDoublePoint>, Serializable {

    /** Serializable version identifier. */
    private static final long serialVersionUID = 8026472786091227632L;

    /** Point coordinates. */
    private final double[] point;

    /**
     * Build an instance wrapping an integer array.
     * <p>
     * The wrapped array is referenced, it is <em>not</em> copied.
     *
     * @param point the n-dimensional point in integer space
     */
    public EuclideanDoublePoint(final double[] point) {
        this.point = point;
    }

    /** {@inheritDoc} */
    public EuclideanDoublePoint centroidOf(final Collection<EuclideanDoublePoint> points) {
        final double[] centroid = new double[getPoint().length];
        for (final EuclideanDoublePoint p : points) {
            for (int i = 0; i < centroid.length; i++) {
                centroid[i] += p.getPoint()[i];
            }
        }
        for (int i = 0; i < centroid.length; i++) {
            centroid[i] /= points.size();
        }
        return new EuclideanDoublePoint(centroid);
    }

    /** {@inheritDoc} */
    public double distanceFrom(final EuclideanDoublePoint p) {
        return MathArrays.distance(point, p.getPoint());
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof EuclideanDoublePoint)) {
            return false;
        }
        return Arrays.equals(point, ((EuclideanDoublePoint) other).point);
    }

    /**
     * Get the n-dimensional point in integer space.
     *
     * @return a reference (not a copy!) to the wrapped array
     */
    public double[] getPoint() {
        return point;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        return Arrays.hashCode(point);
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return Arrays.toString(point);
    }

}
