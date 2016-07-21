package org.apache.diana.api.reader;


import org.apache.diana.api.ReaderField;

/**
 * Class to reads and converts to {@link Integer}, first it verify if is Double if yes return itself then verifies if is
 * {@link Number} and use {@link Number#intValue()} otherwise convert to {@link String} and then {@link Integer}
 *
 * @author Otávio Santana
 */
final class IntegerReader implements ReaderField {

    @Override
    public boolean isCompatible(Class clazz) {
        return Integer.class.equals(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {

        if (Integer.class.isInstance(value)) {
            return (T) value;
        }
        if (Number.class.isInstance(value)) {
            return (T) Integer.valueOf(Number.class.cast(value).intValue());
        } else {
            return (T) Integer.valueOf(value.toString());
        }
    }
}
