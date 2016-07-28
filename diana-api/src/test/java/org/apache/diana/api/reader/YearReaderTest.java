package org.apache.diana.api.reader;

import org.apache.diana.api.ReaderField;
import org.junit.Before;
import org.junit.Test;

import java.time.Year;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Marcelo de Souza
 */
public class YearReaderTest {

    private ReaderField readerField;

    @Before
    public void init() {
        readerField = new YearReader();
    }

    @Test
    public void shouldValidateCompatibility() {
        assertTrue(readerField.isCompatible(Year.class));
        assertFalse(readerField.isCompatible(String.class));
        assertFalse(readerField.isCompatible(Long.class));
    }

    @Test
    public void shouldConvert() {
        Year year = Year.parse("2009");

        assertEquals(year, readerField.read(Year.class, Year.parse("2009")));
        assertEquals(year, readerField.read(String.class, "2009"));
        assertEquals(year, readerField.read(Integer.class, 2009));
        assertEquals(year, readerField.read(Long.class, 2009));
    }


}
