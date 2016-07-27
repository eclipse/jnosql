package org.apache.diana.api.reader;

import org.apache.diana.api.ReaderField;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;


public class BigIntegerReaderTest {

    private ReaderField readerField;

    @Before
    public void init() {
        readerField = new BigIntegerReader();
    }

    @Test
    public void shouldValidateCompatibility() {
        assertTrue(readerField.isCompatible(BigInteger.class));
        assertFalse(readerField.isCompatible(AtomicBoolean.class));
        assertFalse(readerField.isCompatible(Boolean.class));
    }

    @Test
    public void shouldConvert() {
        BigInteger bigInteger = BigInteger.TEN;
        assertEquals(bigInteger, readerField.read(BigInteger.class, bigInteger));
        assertEquals(bigInteger, readerField.read(BigInteger.class, 10.00));
        assertEquals(bigInteger, readerField.read(BigInteger.class, "10"));
    }


}
