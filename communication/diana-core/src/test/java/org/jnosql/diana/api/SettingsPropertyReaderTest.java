package org.jnosql.diana.api;

import org.junit.jupiter.api.Assertions;
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

    @Test
    public void shouldReturnInstanceWhenIsNotString() {
        Object value = reader.apply(123, Settings.builder().build());
        Assertions.assertEquals(123, value);
    }

    @Test
    public void shouldReturnStringWhenThereIsNotEnc() {
        Object value = reader.apply("value", Settings.builder().build());
        Assertions.assertEquals("value", value);
    }

    @Test
    public void shouldDecrypt() {
        Settings settings = Settings.builder()
                .put(SymmetricSettingsEncryption.KEY_PROPERTY, "password")
                .put(SettingsEncryption.ENCRYPTION_TYPE, "symmetric")
                .build();

        String text = "Ada Lovelace";
        SymmetricSettingsEncryption settingsEncryption = new SymmetricSettingsEncryption();
        String encrypt = settingsEncryption.encrypt(text, settings);

        String value = reader.apply("ENC(" + encrypt + ")", settings).toString();
        Assertions.assertEquals(text, value);
    }

}
