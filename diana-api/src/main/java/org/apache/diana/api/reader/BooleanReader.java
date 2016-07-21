package org.apache.diana.api.reader;


import org.apache.diana.api.ReaderField;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class to reads and converts to both {@link Boolean} and {@link AtomicBoolean}
 *
 * @author Otávio Santana
 */
public final class BooleanReader implements ReaderField {

    @Override
    public boolean isCompatible(Class clazz) {
        return Boolean.class.equals(clazz) || AtomicBoolean.class.equals(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {

        boolean isAtomicBoolean = AtomicBoolean.class.equals(clazz);

        if (isAtomicBoolean && AtomicBoolean.class.isInstance(value)) {
            return (T) value;
        }
        Boolean bool = null;
        if (Boolean.class.isInstance(value)) {
            bool = Boolean.class.cast(value);
        } else if (AtomicBoolean.class.isInstance(value)) {
            bool = AtomicBoolean.class.cast(value).get();
        } else if (Number.class.isInstance(value)) {
            bool = Number.class.cast(value).longValue() != 0;
        } else if (String.class.isInstance(value)) {
            bool = Boolean.valueOf(value.toString());
        }

        if (isAtomicBoolean) {
            return (T) new AtomicBoolean(bool);
        }

        return (T) bool;
    }


}
