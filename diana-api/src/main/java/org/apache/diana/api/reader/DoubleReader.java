package org.apache.diana.api.reader;


import org.apache.diana.api.ReaderField;

/**
 * Class to reads and converts to {@link Double}, first it verify if is Double if yes return itself then verifies if is
 * {@link Number} and use {@link Number#doubleValue()} otherwise convert to {@link String} and then {@link Double}
 *
 * @author Otávio Santana
 */
final class DoubleReader implements ReaderField {

    @Override
    public boolean isCompatible(Class clazz) {
        return Double.class.equals(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {

        if (Double.class.isInstance(value)) {
            return (T) value;
        }
        if (Number.class.isInstance(value)) {
            return (T) Double.valueOf(Number.class.cast(value).doubleValue());
        } else {
            return (T) Double.valueOf(value.toString());
        }
    }
}
