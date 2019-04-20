/*
 *
 *  Copyright (c) 2019 Otávio Santana and others
 *   All rights reserved. This program and the accompanying materials
 *   are made available under the terms of the Eclipse Public License v1.0
 *   and Apache License v2.0 which accompanies this distribution.
 *   The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html
 *   and the Apache License v2.0 is available at http://www.opensource.org/licenses/apache2.0.php.
 *
 *   You may elect to redistribute this code under either of these licenses.
 *
 *   Contributors:
 *
 *   Otavio Santana
 *
 */
package org.jnosql.diana.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SettingsPropertyReaderTest {

    private SettingsPropertyReader reader;

    @BeforeEach
    public void before() {
        this.reader = SettingsPropertyReader.INSTANCE;
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
