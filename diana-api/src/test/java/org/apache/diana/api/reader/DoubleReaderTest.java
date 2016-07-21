package org.apache.diana.api.reader;

import org.apache.diana.api.ReaderField;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;


public class DoubleReaderTest {

    private ReaderField readerField;

    @Before
    public void init() {
        readerField = new DoubleReader();
    }

    @Test
    public void shouldReturnTrueWhenClassIsNumber() {
        assertTrue(readerField.isCompatible(Double.class));
        assertFalse(readerField.isCompatible(AtomicBoolean.class));
        assertFalse(readerField.isCompatible(Boolean.class));
    }

    @Test
    public void shouldConvert() {
        Double number = 10D;
        assertEquals(number, readerField.read(Double.class, number));
        assertEquals(Double.valueOf(10D), readerField.read(Double.class, 10.00));
        assertEquals(Double.valueOf(10D), readerField.read(Double.class, "10"));
    }


}
