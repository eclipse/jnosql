package org.apache.diana.api.reader;


import org.apache.diana.api.ReaderField;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class to reads and converts to both {@link String} and {@link CharSequence}.
 *
 * @author Otávio Santana
 */
public final class StringReader implements ReaderField {

    @Override
    public boolean isCompatible(Class clazz) {
        return CharSequence.class.equals(clazz) || String.class.equals(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {

        boolean isClazzString = String.class.equals(clazz);

        if (CharSequence.class.equals(clazz) && CharSequence.class.isInstance(value)) {
            return (T) value;
        }
        return (T) value.toString();
    }


}
