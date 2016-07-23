package org.apache.diana.api.reader;


import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DateReaderTest {

    private DateReader dateReader;

    @Before
    public void init() {
        dateReader = new DateReader();
    }

    @Test
    public void shouldValidateCompatibility() {
        assertTrue(dateReader.isCompatible(Date.class));
    }

    @Test
    public void shouldConvert() {
        long milliseconds = new Date().getTime();
        Date date = new Date();
        assertEquals(milliseconds, dateReader.read(Date.class, milliseconds).getTime());
        assertEquals(date, dateReader.read(Date.class, date));
    }
}
