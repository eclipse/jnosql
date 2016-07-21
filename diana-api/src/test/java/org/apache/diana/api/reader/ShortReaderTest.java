package org.apache.diana.api.reader;

import org.apache.diana.api.ReaderField;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;


public class ShortReaderTest {

    private ReaderField readerField;

    @Before
    public void init() {
        readerField = new ShortReader();
    }

    @Test
    public void shouldValidateCompatibility() {
        assertTrue(readerField.isCompatible(Short.class));
        assertFalse(readerField.isCompatible(AtomicBoolean.class));
        assertFalse(readerField.isCompatible(Boolean.class));
    }

    @Test
    public void shouldConvert() {
        Short number = Short.valueOf((short)10);

        assertEquals(number, readerField.read(Short.class, number));
        assertEquals(Short.valueOf((short)10), readerField.read(Short.class, 10.00));
        assertEquals(Short.valueOf((short)10), readerField.read(Short.class, "10"));
    }


}
