package org.apache.diana.api.reader;

import org.apache.diana.api.ReaderField;

import java.time.Year;
import java.util.Calendar;

/**
 * Class to reads and converts to {@link Year}, first it verify if is Year if yes return itself otherwise convert to {@link String}
 *
 * @author Marcelo de Souza
 */
public final class YearReader  implements ReaderField {

    @Override
    public boolean isCompatible(Class clazz) {
        return Year.class.equals(clazz);
    }

    @Override
    public <T> T read(Class<T> clazz, Object value) {

        if (Year.class.isInstance(value)) {
            return (T) value;
        }

        Year year = Year.parse(value.toString());
        return (T) year;
    }
}
