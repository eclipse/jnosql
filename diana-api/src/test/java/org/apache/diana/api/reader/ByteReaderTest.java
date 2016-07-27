package org.apache.diana.api.reader;

import org.apache.diana.api.ReaderField;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;


public class ByteReaderTest {

    private ReaderField readerField;

    @Before
    public void init() {
        readerField = new ByteReader();
    }

    @Test
    public void shouldValidateCompatibility() {
        assertTrue(readerField.isCompatible(Byte.class));
        assertFalse(readerField.isCompatible(AtomicBoolean.class));
        assertFalse(readerField.isCompatible(Boolean.class));
    }

    @Test
    public void shouldConvert() {
        Byte number = (byte) 10;
        assertEquals(number, readerField.read(Byte.class, 10.00));
        assertEquals(Byte.valueOf((byte) 10), readerField.read(Byte.class, 10.00));
        assertEquals(Byte.valueOf((byte) 10), readerField.read(Byte.class, "10"));
    }


}
