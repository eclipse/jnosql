package org.apache.diana.api.reader;


import org.apache.diana.api.ReaderField;

/**
 * Class to reads and converts to {@link Byte}, first it verify if is Double if yes return itself then verifies if is
 * {@link Number} and use {@link Number#byteValue()} otherwise convert to {@link String} and then {@link Byte}
 *
 * @author Otávio Santana
 */
public final class ByteReader implements ReaderField {

    @Override
    public boolean isCompatible(Class clazz) {
        return Byte.class.equals(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {

        if (Byte.class.isInstance(value)) {
            return (T) value;
        }
        if (Number.class.isInstance(value)) {
            return (T) Byte.valueOf(Number.class.cast(value).byteValue());
        } else {
            return (T) Byte.valueOf(value.toString());
        }
    }
}
