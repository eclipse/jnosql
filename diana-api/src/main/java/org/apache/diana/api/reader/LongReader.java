package org.apache.diana.api.reader;


import org.apache.diana.api.ReaderField;

/**
 * Class to reads and converts to {@link Long}, first it verify if is Double if yes return itself then verifies if is
 * {@link Number} and use {@link Number#longValue()} otherwise convert to {@link String} and then {@link Long}
 *
 * @author Otávio Santana
 */
public final class LongReader implements ReaderField {

    @Override
    public boolean isCompatible(Class clazz) {
        return Long.class.equals(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {

        if (Long.class.isInstance(value)) {
            return (T) value;
        }
        if (Number.class.isInstance(value)) {
            return (T) Long.valueOf(Number.class.cast(value).longValue());
        } else {
            return (T) Long.valueOf(value.toString());
        }
    }
}
