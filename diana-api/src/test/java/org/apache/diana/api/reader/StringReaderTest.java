package org.apache.diana.api.reader;

import org.apache.diana.api.ReaderField;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;


public class StringReaderTest {

    private ReaderField readerField;

    @Before
    public void init() {
        readerField = new StringReader();
    }

    @Test
    public void shouldReturnTrueWhenClassIsNumber() {
        assertTrue(readerField.isCompatible(String.class));
        assertTrue(readerField.isCompatible(CharSequence.class));
        assertFalse(readerField.isCompatible(AtomicBoolean.class));
    }

    @Test
    public void shouldConvert() {
        StringBuilder stringBuilder = new StringBuilder("sb");

        assertEquals(stringBuilder, readerField.read(CharSequence.class, stringBuilder));
        assertEquals(stringBuilder.toString(), readerField.read(String.class, stringBuilder));

        assertEquals("10", readerField.read(CharSequence.class, 10));
        assertEquals("10.0", readerField.read(String.class, 10.00));
        assertEquals("10", readerField.read(CharSequence.class, "10"));
        assertEquals("10", readerField.read(String.class, "10"));
    }


}
