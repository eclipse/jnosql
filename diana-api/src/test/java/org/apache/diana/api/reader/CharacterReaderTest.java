package org.apache.diana.api.reader;

import org.apache.diana.api.ReaderField;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.*;


public class CharacterReaderTest {

    private ReaderField readerField;

    @Before
    public void init() {
        readerField = new CharacterReader();
    }

    @Test
    public void shouldReturnTrueWhenClassIsNumber() {
        assertTrue(readerField.isCompatible(Character.class));
        assertFalse(readerField.isCompatible(AtomicBoolean.class));
    }

    @Test
    public void shouldConvert() {
        Character character = 'o';
        assertEquals(character, readerField.read(Character.class, character));
        assertEquals(Character.valueOf((char) 10), readerField.read(Character.class, 10.00));
        assertEquals(Character.valueOf('1'), readerField.read(Character.class, "10"));
        assertEquals(Character.valueOf(Character.MIN_VALUE), readerField.read(Character.class, ""));
    }


}
