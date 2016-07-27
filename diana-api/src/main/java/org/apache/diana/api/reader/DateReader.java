package org.apache.diana.api.reader;


import org.apache.diana.api.ReaderField;

import java.util.Date;

/**
 * Class to reads and converts Date type
 *
 * @author Daniel Cunha danielsoro@apache.org
 */
public final class DateReader implements ReaderField {

    @Override
    public boolean isCompatible(Class clazz) {
        return Date.class.equals(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {

        if (Date.class.isInstance(value)) {
            return (T) value;
        }

        if (Long.class.isInstance(value)) {
            return (T) new Date((Long) value);
        }
        return (T) new Date(value.toString());
    }
}
