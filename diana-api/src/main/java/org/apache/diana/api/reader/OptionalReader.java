package org.apache.diana.api.reader;


import org.apache.diana.api.ReaderField;

import java.util.Optional;

/**
 * Class to reads and converts to {@link Optional}
 *
 * @author Otávio Santana
 */
public final class OptionalReader implements ReaderField {

    @Override
    public boolean isCompatible(Class clazz) {
        return Optional.class.equals(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {

        if (Optional.class.isInstance(value)) {
            return (T) value;
        }
        return (T) Optional.ofNullable(value);
    }


}
