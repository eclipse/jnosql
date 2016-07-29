package org.apache.diana.api.reader;


import org.apache.diana.api.ReaderField;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class to reads and converts to {@link AtomicInteger}, first it verify if is AtomicInteger if yes return itself then verifies if is
 * {@link Number} and use {@link Number#intValue()} ()} otherwise convert to {@link String} and then {@link AtomicInteger}
 *
 * @author Otávio Santana
 */
public final class AtomicIntegerReader implements ReaderField<AtomicInteger> {

    @Override
    public boolean isCompatible(Class clazz) {
        return AtomicInteger.class.equals(clazz);
    }

    @Override
    public AtomicInteger read(Object value) {

        if (AtomicInteger.class.isInstance(value)) {
            return (AtomicInteger) value;
        }
        if (Number.class.isInstance(value)) {
            return new AtomicInteger(Number.class.cast(value).intValue());
        } else {
            return new AtomicInteger(Integer.valueOf(value.toString()));
        }
    }


}
