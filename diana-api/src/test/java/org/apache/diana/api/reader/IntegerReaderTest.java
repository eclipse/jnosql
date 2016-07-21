package org.apache.diana.api.reader;

import org.apache.diana.api.ReaderField;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;


public class IntegerReaderTest {

    private ReaderField readerField;

    @Before
    public void init() {
        readerField = new IntegerReader();
    }

    @Test
    public void shouldValidateCompatibility() {
        assertTrue(readerField.isCompatible(Integer.class));
        assertFalse(readerField.isCompatible(AtomicBoolean.class));
        assertFalse(readerField.isCompatible(Boolean.class));
    }

    @Test
    public void shouldConvert() {
        Integer number = 10;
        assertEquals(Integer.valueOf(10), readerField.read(Integer.class, number));
        assertEquals(Integer.valueOf(10), readerField.read(Integer.class, 10.00));
        assertEquals(Integer.valueOf(10), readerField.read(Integer.class, "10"));
    }


}
