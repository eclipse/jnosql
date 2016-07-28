package org.apache.diana.api.reader;

import org.apache.diana.api.ReaderField;

import java.util.Calendar;
import java.util.Date;

/**
 * Class to reads and converts to {@link Calendar}, first it verify if is Calendar if yes return itself then verifies if is
 * {@link Long} and use {@link Calendar#setTimeInMillis(long)}} otherwise convert to {@link String}
 *
 * @author Marcelo de Souza
 */
public final class CalendarReader implements ReaderField {

    @Override
    public boolean isCompatible(Class clazz) {
        return Calendar.class.equals(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {

        if (Calendar.class.isInstance(value)) {
            return (T) value;
        }

        if (Number.class.isInstance(value)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis( ((Number) value).longValue());
            return (T) calendar;
        }

        if (Date.class.isInstance(value)) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date)value);
            return (T) calendar;
        }

        Date date = new Date(value.toString());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return (T) calendar;
    }
}
