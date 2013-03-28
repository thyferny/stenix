package math.util;

import exception.MathIllegalArgumentException;

public interface NumberTransformer {

    /**
     * Implementing this interface provides a facility to transform
     * from Object to Double.
     *
     * @param o the Object to be transformed.
     * @return the double value of the Object.
     * @throws MathIllegalArgumentException if the Object can not be transformed into a Double.
     */
    double transform(Object o) throws MathIllegalArgumentException;
}
