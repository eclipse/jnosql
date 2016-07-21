package org.apache.diana.api.reader;

import org.apache.diana.api.ReaderField;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;


public class BigDecimalReaderTest {

    private ReaderField readerField;

    @Before
    public void init() {
        readerField = new BigDecimalReader();
    }

    @Test
    public void shouldValidateCompatibility() {
        assertTrue(readerField.isCompatible(BigDecimal.class));
        assertFalse(readerField.isCompatible(AtomicBoolean.class));
        assertFalse(readerField.isCompatible(Boolean.class));
    }

    @Test
    public void shouldConvert() {
        BigDecimal bigDecimal = BigDecimal.TEN;
        assertEquals(bigDecimal, readerField.read(BigDecimal.class, bigDecimal));
        assertEquals(BigDecimal.valueOf(10D), readerField.read(BigDecimal.class, 10.00));
        assertEquals(BigDecimal.valueOf(10D), readerField.read(BigDecimal.class, "10"));
    }


}
