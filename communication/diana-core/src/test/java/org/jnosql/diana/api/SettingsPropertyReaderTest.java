package org.jnosql.diana.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SettingsPropertyReaderTest {

    private SettingsPropertyReader reader;

    @BeforeEach
    public void before() {
        this.reader = new SettingsPropertyReader();
    }

    @Test
    public void shouldReturnMatch() {
        assertTrue(reader.isValid("ENC(value)"));
        assertTrue(reader.isValid("ENC(asdfasdfa)"));
    }


    @Test
    public void shouldNotReturnMatch() {
        assertFalse(reader.isValid("ENC(value);"));
        assertFalse(reader.isValid("ENC(value"));
        assertFalse(reader.isValid("ENCvalue)"));
        assertFalse(reader.isValid("EN(value)"));
        assertFalse(reader.isValid("ENC()"));
    }

    @Test
    public void shouldExtract() {
        assertEquals("value", reader.extract("ENC(value)"));
        assertEquals("asdfasdfa", reader.extract("ENC(asdfasdfa)"));
    }

}
