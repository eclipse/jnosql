package org.apache.diana.api.reader;

import org.apache.diana.api.ReaderField;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;


public class LongReaderTest {

    private ReaderField readerField;

    @Before
    public void init() {
        readerField = new LongReader();
    }

    @Test
    public void shouldValidateCompatibility() {
        assertTrue(readerField.isCompatible(Long.class));
        assertFalse(readerField.isCompatible(AtomicBoolean.class));
        assertFalse(readerField.isCompatible(Boolean.class));
    }

    @Test
    public void shouldConvert() {
        Long number = 10L;
        assertEquals(number, readerField.read(Number.class, number));
        assertEquals(Long.valueOf(10L), readerField.read(Long.class, 10.00));
        assertEquals(Long.valueOf(10L), readerField.read(Long.class, "10"));
    }


}
