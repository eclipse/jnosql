package org.apache.diana.api.reader;

import org.apache.diana.api.ReaderField;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;

import static org.junit.Assert.*;

/**
 * @author Marcelo de Souza
 */
public class CalendarReaderTest {

    private ReaderField readerField;

    @Before
    public void init() {
        readerField = new CalendarReader();
    }

    @Test
    public void shouldValidateCompatibility() {
        assertTrue(readerField.isCompatible(Calendar.class));
        assertFalse(readerField.isCompatible(String.class));
        assertFalse(readerField.isCompatible(Long.class));
    }

    @Test
    public void shouldConvert() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2010, 10, 9);

        assertEquals(calendar, readerField.read(Calendar.class, calendar));
        assertEquals(calendar, readerField.read(Calendar.class, calendar.getTimeInMillis()));
    }


}
