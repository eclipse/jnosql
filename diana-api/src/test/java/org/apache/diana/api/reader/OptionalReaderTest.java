package org.apache.diana.api.reader;

import org.apache.diana.api.ReaderField;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;


public class OptionalReaderTest {

    private ReaderField readerField;

    @Before
    public void init() {
        readerField = new OptionalReader();
    }

    @Test
    public void shouldReturnTrueWhenClassIsNumber() {
        assertTrue(readerField.isCompatible(Optional.class));
        assertFalse(readerField.isCompatible(AtomicBoolean.class));
    }

    @Test
    public void shouldConvert() {
        Optional<String> optional = Optional.of("value");
        assertEquals(optional, readerField.read(Optional.class, optional));

        Optional<String> result = readerField.read(Optional.class, "12");
        assertEquals(Optional.of("12"), result);

    }


}
