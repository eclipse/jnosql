package org.apache.diana.api.reader;

import org.apache.diana.api.ReaderField;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;


public class NumberReaderTest {

    private ReaderField readerField;

    @Before
    public void init() {
        readerField = new NumberReader();
    }

    @Test
    public void shouldReturnTrueWhenClassIsNumber() {
        assertTrue(readerField.isCompatible(Number.class));
        assertFalse(readerField.isCompatible(AtomicBoolean.class));
        assertFalse(readerField.isCompatible(Boolean.class));
    }

    @Test
    public void shouldConvert() {
        assertEquals(10D, readerField.read(Number.class, 10.00));
        assertEquals(10D, readerField.read(Number.class, "10"));
    }


}
