package org.apache.diana.api.reader;


import org.apache.diana.api.ReaderField;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Class to reads and converts to {@link AtomicLong}, first it verify if is Double if yes return itself then verifies if is
 * {@link Number} and use {@link Number#longValue()} otherwise convert to {@link String} and then {@link AtomicLong}
 *
 * @author Otávio Santana
 */
public final class AtomicLongReader implements ReaderField {



    @Override
    public boolean isCompatible(Class clazz) {
        return AtomicLong.class.equals(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {

        if (AtomicLong.class.isInstance(value)) {
            return (T) value;
        }
        if (Number.class.isInstance(value)) {
            return (T) new AtomicLong(Long.valueOf(Number.class.cast(value).longValue()));
        } else {
            return (T) new AtomicLong(Long.valueOf(value.toString()));
        }
    }

}
