package org.jnosql.diana.reader;

import jakarta.nosql.ValueReader;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

/**
 * Class to reads and converts to {@link OffsetDateTime} type
 *
 */
public class OffsetDateTimeValueReader implements ValueReader {

    @Override
    public <T> boolean isCompatible(Class<T> typeClass) {
        return OffsetDateTime.class.equals(typeClass);
    }

    @Override
    public <T> T read(Class<T> typeClass, Object value) {

        return (T) getOffSetDateTime(value);
    }

    private OffsetDateTime getOffSetDateTime(Object value) {
        if (OffsetDateTime.class.isInstance(value)) {
            return OffsetDateTime.class.cast(value);
        }

        if (Calendar.class.isInstance(value)) {
            return ((Calendar) value).toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime();
        }

        if (Date.class.isInstance(value)) {
            return ((Date) value).toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime();
        }

        if (Number.class.isInstance(value)) {
            return new Date(((Number) value).longValue()).toInstant().atZone(ZoneId.systemDefault()).toOffsetDateTime();
        }

        return OffsetDateTime.parse(value.toString());
    }
}
