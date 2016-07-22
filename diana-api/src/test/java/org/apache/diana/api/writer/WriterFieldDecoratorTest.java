package org.apache.diana.api.writer;

import org.apache.diana.api.WriterField;
import org.junit.Before;
import org.junit.Test;

import java.time.temporal.Temporal;
import java.util.Optional;

import static org.junit.Assert.*;

public class WriterFieldDecoratorTest {

    private WriterField writerField;

    @Before
    public void setUp() {
        writerField = WriterFieldDecorator.getInstance();
    }

    @Test
    public void shouldVerifyCompatibility() {
        assertTrue(writerField.isCompatible(Optional.class));
        assertTrue(writerField.isCompatible(Temporal.class));
        assertFalse(writerField.isCompatible(Boolean.class));
    }

    @Test
    public void shouldConvert() {
        String diana = "diana";
        Optional<String> optinal = Optional.of(diana);
        Object result = writerField.write(optinal);
        assertEquals(diana, result);
    }
}