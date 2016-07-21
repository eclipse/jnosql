package org.apache.diana.api.reader;


import org.apache.diana.api.ReaderField;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class to reads and converts to {@link AtomicInteger}, first it verify if is AtomicInteger if yes return itself then verifies if is
 * {@link Number} and use {@link Number#intValue()} ()} otherwise convert to {@link String} and then {@link AtomicInteger}
 *
 * @author Otávio Santana
 */
final class AtomicIntegerReader implements ReaderField {

    @Override
    public boolean isCompatible(Class clazz) {
        return AtomicInteger.class.equals(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {

        if (AtomicInteger.class.isInstance(value)) {
            return (T) value;
        }
        if (Number.class.isInstance(value)) {
            return (T) new AtomicInteger(Number.class.cast(value).intValue());
        } else {
            return (T) new AtomicInteger(Integer.valueOf(value.toString()));
        }
    }
}
