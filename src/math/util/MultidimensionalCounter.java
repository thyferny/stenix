package math.util;

import exception.DimensionMismatchException;
import exception.NotStrictlyPositiveException;
import exception.OutOfRangeException;

public class MultidimensionalCounter implements Iterable<Integer> {
    /**
     * Number of dimensions.
     */
    private final int dimension;
    /**
     * Offset for each dimension.
     */
    private final int[] uniCounterOffset;
    /**
     * Counter sizes.
     */
    private final int[] size;
    /**
     * Total number of (one-dimensional) slots.
     */
    private final int totalSize;
    /**
     * Index of last dimension.
     */
    private final int last;

    /**
     * Perform iteration over the multidimensional counter.
     */
    public class Iterator implements java.util.Iterator<Integer> {
        /**
         * Multidimensional counter.
         */
        private final int[] counter = new int[dimension];
        /**
         * Unidimensional counter.
         */
        private int count = -1;

        /**
         * Create an iterator
         * @see #iterator()
         */
        Iterator() {
            counter[last] = -1;
        }

        /**
         * {@inheritDoc}
         */
        public boolean hasNext() {
            for (int i = 0; i < dimension; i++) {
                if (counter[i] != size[i] - 1) {
                    return true;
                }
            }
            return false;
        }

        /**
         * @return the unidimensional count after the counter has been
         * incremented by {@code 1}.
         */
        public Integer next() {
            for (int i = last; i >= 0; i--) {
                if (counter[i] == size[i] - 1) {
                    counter[i] = 0;
                } else {
                    ++counter[i];
                    break;
                }
            }

            return ++count;
        }

        /**
         * Get the current unidimensional counter slot.
         *
         * @return the index within the unidimensionl counter.
         */
        public int getCount() {
            return count;
        }
        /**
         * Get the current multidimensional counter slots.
         *
         * @return the indices within the multidimensional counter.
         */
        public int[] getCounts() {
            return MathArrays.copyOf(counter);
        }

        /**
         * Get the current count in the selected dimension.
         *
         * @param dim Dimension index.
         * @return the count at the corresponding index for the current state
         * of the iterator.
         * @throws IndexOutOfBoundsException if {@code index} is not in the
         * correct interval (as defined by the length of the argument in the
         * {@link MultidimensionalCounter#MultidimensionalCounter(int[])
         * constructor of the enclosing class}).
         */
        public int getCount(int dim) {
            return counter[dim];
        }

        /**
         * @throws UnsupportedOperationException
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Create a counter.
     *
     * @param size Counter sizes (number of slots in each dimension).
     * @throws NotStrictlyPositiveException if one of the sizes is
     * negative or zero.
     */
    public MultidimensionalCounter(int ... size) throws NotStrictlyPositiveException {
        dimension = size.length;
        this.size = MathArrays.copyOf(size);

        uniCounterOffset = new int[dimension];

        last = dimension - 1;
        int tS = size[last];
        for (int i = 0; i < last; i++) {
            int count = 1;
            for (int j = i + 1; j < dimension; j++) {
                count *= size[j];
            }
            uniCounterOffset[i] = count;
            tS *= size[i];
        }
        uniCounterOffset[last] = 0;

        if (tS <= 0) {
            throw new NotStrictlyPositiveException(tS);
        }

        totalSize = tS;
    }

    /**
     * Create an iterator over this counter.
     *
     * @return the iterator.
     */
    public Iterator iterator() {
        return new Iterator();
    }

    /**
     * Get the number of dimensions of the multidimensional counter.
     *
     * @return the number of dimensions.
     */
    public int getDimension() {
        return dimension;
    }

    /**
     * Convert to multidimensional counter.
     *
     * @param index Index in unidimensional counter.
     * @return the multidimensional counts.
     * @throws OutOfRangeException if {@code index} is not between
     * {@code 0} and the value returned by {@link #getSize()} (excluded).
     */
    public int[] getCounts(int index) throws OutOfRangeException {
        if (index < 0 ||
            index >= totalSize) {
            throw new OutOfRangeException(index, 0, totalSize);
        }

        final int[] indices = new int[dimension];

        int count = 0;
        for (int i = 0; i < last; i++) {
            int idx = 0;
            final int offset = uniCounterOffset[i];
            while (count <= index) {
                count += offset;
                ++idx;
            }
            --idx;
            count -= offset;
            indices[i] = idx;
        }

        indices[last] = index - count;

        return indices;
    }

    /**
     * Convert to unidimensional counter.
     *
     * @param c Indices in multidimensional counter.
     * @return the index within the unidimensionl counter.
     * @throws DimensionMismatchException if the size of {@code c}
     * does not match the size of the array given in the constructor.
     * @throws OutOfRangeException if a value of {@code c} is not in
     * the range of the corresponding dimension, as defined in the
     * {@link MultidimensionalCounter#MultidimensionalCounter(int...) constructor}.
     */
    public int getCount(int ... c)
        throws OutOfRangeException, DimensionMismatchException {
        if (c.length != dimension) {
            throw new DimensionMismatchException(c.length, dimension);
        }
        int count = 0;
        for (int i = 0; i < dimension; i++) {
            final int index = c[i];
            if (index < 0 ||
                index >= size[i]) {
                throw new OutOfRangeException(index, 0, size[i] - 1);
            }
            count += uniCounterOffset[i] * c[i];
        }
        return count + c[last];
    }

    /**
     * Get the total number of elements.
     *
     * @return the total size of the unidimensional counter.
     */
    public int getSize() {
        return totalSize;
    }
    /**
     * Get the number of multidimensional counter slots in each dimension.
     *
     * @return the sizes of the multidimensional counter in each dimension.
     */
    public int[] getSizes() {
        return MathArrays.copyOf(size);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dimension; i++) {
            sb.append("[").append(getCount(i)).append("]");
        }
        return sb.toString();
    }
}
