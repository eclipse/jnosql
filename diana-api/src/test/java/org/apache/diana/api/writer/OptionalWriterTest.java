package org.apache.diana.api.writer;

import org.apache.diana.api.WriterField;
import org.junit.Before;
import org.junit.Test;

import java.time.*;
import java.time.temporal.Temporal;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by otaviojava on 22/07/16.
 */
public class OptionalWriterTest {

    private WriterField<Optional, String> writerField;

    @Before
    public void setUp() {
        writerField = new OptionalWriter();
    }

    @Test
    public void shouldVerifyCompatibility() {
        assertTrue(writerField.isCompatible(Optional.class));
        assertFalse(writerField.isCompatible(Boolean.class));
    }

    @Test
    public void shouldConvert() {
        String diana = "diana";
        Optional<String> optinal = Optional.of(diana);
        String result = writerField.write(optinal);
        assertEquals(diana, result);
    }
}