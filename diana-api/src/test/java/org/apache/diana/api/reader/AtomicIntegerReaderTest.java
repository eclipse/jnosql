package org.apache.diana.api.reader;

import org.apache.diana.api.ReaderField;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;


public class AtomicIntegerReaderTest {

    private ReaderField readerField;

    @Before
    public void init() {
        readerField = new AtomicIntegerReader();
    }

    @Test
    public void shouldReturnTrueWhenClassIsNumber() {
        assertTrue(readerField.isCompatible(AtomicInteger.class));
        assertFalse(readerField.isCompatible(AtomicBoolean.class));
    }

    @Test
    public void shouldConvert() {
        AtomicInteger integer = new AtomicInteger(10);
        assertEquals(integer, readerField.read(AtomicInteger.class, integer));
        assertEquals(integer.get(), readerField.read(AtomicInteger.class, 10.00).get());
        assertEquals(integer.get(), readerField.read(AtomicInteger.class, "10").get());
    }


}
