package org.apache.diana.api.reader;


import org.apache.diana.api.ReaderField;

/**
 * Class to reads and converts to {@link Float}, first it verify if is Double if yes return itself then verifies if is
 * {@link Number} and use {@link Number#floatValue()} otherwise convert to {@link String} and then {@link Float}
 *
 * @author Otávio Santana
 */
final class FloatReader implements ReaderField {

    @Override
    public boolean isCompatible(Class clazz) {
        return Float.class.equals(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {

        if (Float.class.isInstance(value)) {
            return (T) value;
        }
        if (Number.class.isInstance(value)) {
            return (T) Float.valueOf(Number.class.cast(value).floatValue());
        } else {
            return (T) Float.valueOf(value.toString());
        }
    }
}
