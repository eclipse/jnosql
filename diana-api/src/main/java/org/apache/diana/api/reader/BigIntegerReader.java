package org.apache.diana.api.reader;


import org.apache.diana.api.ReaderField;

import java.math.BigInteger;

/**
 * Class to reads and converts to {@link BigInteger}, first it verify if is Double if yes return itself then verifies if is
 * {@link Number} and use {@link Number#longValue()} otherwise convert to {@link String} and then {@link BigInteger}
 *
 * @author Otávio Santana
 */
final class BigIntegerReader implements ReaderField {

    @Override
    public boolean isCompatible(Class clazz) {
        return BigInteger.class.equals(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {

        if (BigInteger.class.isInstance(value)) {
            return (T) value;
        }
        if (Number.class.isInstance(value)) {
            return (T) BigInteger.valueOf(Long.valueOf(Number.class.cast(value).longValue()));
        } else {
            return (T) BigInteger.valueOf(Long.valueOf(value.toString()));
        }
    }
}
