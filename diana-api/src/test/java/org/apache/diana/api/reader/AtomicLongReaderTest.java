package org.apache.diana.api.reader;

import org.apache.diana.api.ReaderField;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.Assert.*;


public class AtomicLongReaderTest {

    private ReaderField readerField;

    @Before
    public void init() {
        readerField = new AtomicLongReader();
    }

    @Test
    public void shouldValidateCompatibility() {
        assertTrue(readerField.isCompatible(AtomicLong.class));
        assertFalse(readerField.isCompatible(AtomicBoolean.class));
        assertFalse(readerField.isCompatible(Boolean.class));
    }

    @Test
    public void shouldConvert() {
        AtomicLong atomicLong = new AtomicLong(9L);
        assertEquals(atomicLong, readerField.read(AtomicLong.class, atomicLong));
        assertEquals(atomicLong.get(), readerField.read(AtomicLong.class, 9.00).get());
        assertEquals(atomicLong.get(), readerField.read(AtomicLong.class, "9").get());
    }


}
