package org.apache.diana.api.reader;

import org.apache.diana.api.ReaderField;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;


public class FloatReaderTest {

    private ReaderField readerField;

    @Before
    public void init() {
        readerField = new FloatReader();
    }

    @Test
    public void shouldReturnTrueWhenClassIsNumber() {
        assertTrue(readerField.isCompatible(Float.class));
        assertFalse(readerField.isCompatible(AtomicBoolean.class));
        assertFalse(readerField.isCompatible(Boolean.class));
    }

    @Test
    public void shouldConvert() {
        Float number = 10F;
        assertEquals(number, readerField.read(Float.class, number));
        assertEquals(Float.valueOf(10F), readerField.read(Float.class, 10.00));
        assertEquals(Float.valueOf(10F), readerField.read(Float.class, "10"));
    }


}
