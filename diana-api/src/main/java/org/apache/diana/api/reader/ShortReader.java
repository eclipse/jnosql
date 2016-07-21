package org.apache.diana.api.reader;


import org.apache.diana.api.ReaderField;

/**
 * Class to reads and converts to {@link Short}, first it verify if is Double if yes return itself then verifies if is
 * {@link Number} and use {@link Number#shortValue()} otherwise convert to {@link String} and then {@link Short}
 *
 * @author Otávio Santana
 */
public final class ShortReader implements ReaderField {

    @Override
    public boolean isCompatible(Class clazz) {
        return Short.class.equals(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {

        if (Short.class.isInstance(value)) {
            return (T) value;
        }
        if (Number.class.isInstance(value)) {
            return (T) Short.valueOf(Number.class.cast(value).shortValue());
        } else {
            return (T) Short.valueOf(value.toString());
        }
    }
}
