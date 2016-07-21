package org.apache.diana.api.reader;

import org.apache.diana.api.ReaderField;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;


public class EnumReaderTest {

    private ReaderField readerField;

    @Before
    public void init() {
        readerField = new EnumReader();
    }

    @Test
    public void shouldReturnTrueWhenClassIsNumber() {
        assertTrue(readerField.isCompatible(Enum.class));
        assertTrue(readerField.isCompatible(ExampleNumber.class));
        assertFalse(readerField.isCompatible(AtomicBoolean.class));
    }

    @Test
    public void shouldConvert() {
        ExampleNumber value = ExampleNumber.ONE;
        assertEquals(value, readerField.read(ExampleNumber.class, value));
        assertEquals(value, readerField.read(ExampleNumber.class, 0));
        assertEquals(value, readerField.read(ExampleNumber.class, "ONE"));
        assertEquals(ExampleNumber.TWO, readerField.read(ExampleNumber.class, 1));
        assertEquals(ExampleNumber.TWO, readerField.read(ExampleNumber.class, "TWO"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldReturnErrorInIndex() {
        readerField.read(ExampleNumber.class, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldReturnErrorInName() {
        readerField.read(ExampleNumber.class, "FOUR");
    }


    enum ExampleNumber {
        ONE, TWO
    }


}
