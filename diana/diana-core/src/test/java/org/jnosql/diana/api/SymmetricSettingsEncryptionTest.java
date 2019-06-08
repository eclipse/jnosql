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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SymmetricSettingsEncryptionTest {

    private SettingsEncryption settingsEncryption;

    @BeforeEach
    public void setUp() {
        this.settingsEncryption = new SymmetricSettingsEncryption();
    }

    @Test
    public void shouldEncrypt() {
        Settings settings = Settings.builder()
                .put(SymmetricSettingsEncryption.PASSWORD_PROPERTY, "password")
                .build();
        String encrypt = settingsEncryption.encrypt("Ada Lovelace", settings);
        assertNotNull(encrypt);
    }

    @Test
    public void shouldReturnErrorWhenThereIsNotPassword() {
        Settings settings = Settings.of(Collections.emptyMap());
        assertThrows(EncryptionException.class, () -> settingsEncryption.encrypt("Ada Lovelace", settings) );
        assertThrows(EncryptionException.class, () -> settingsEncryption.decrypt("Ada Lovelace", settings) );

    }

    @Test
    public void shouldDecrypt() {
        Settings settings = Settings.builder()
                .put(SymmetricSettingsEncryption.PASSWORD_PROPERTY, "password")
                .build();
        String text = "Ada Lovelace";
        String encrypt = settingsEncryption.encrypt(text, settings);
        String decrypt = settingsEncryption.decrypt(encrypt, settings);
        assertNotNull(encrypt);
        assertNotNull(decrypt);
        assertEquals(text, decrypt);
    }

    @Test
    public void shouldAllowToUseDifferentCrypt() {
        Settings settings = Settings.builder()
                .put(SymmetricSettingsEncryption.PASSWORD_PROPERTY, "password")
                .put(SymmetricSettingsEncryption.CRYPT_PROPERTY, "AES")
                .build();

        String text = "Ada Lovelace";
        String encrypt = settingsEncryption.encrypt(text, settings);
        String decrypt = settingsEncryption.decrypt(encrypt, settings);
        assertNotNull(encrypt);
        assertNotNull(decrypt);
        assertEquals(text, decrypt);
    }


}