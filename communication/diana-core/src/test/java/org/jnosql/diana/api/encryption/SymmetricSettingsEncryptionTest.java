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
package org.jnosql.diana.api.encryption;

import org.jnosql.diana.api.Settings;
import org.jnosql.diana.api.SettingsEncryption;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SymmetricSettingsEncryptionTest {

    private SettingsEncryption settingsEncryption;

    @BeforeEach
    public void setUp() {
        this.settingsEncryption = new SymmetricSettingsEncryption();
    }

    @Test
    public void shouldEncrypt() {
        Settings settings = Settings.builder()
                .put(SymmetricSettingsEncryption.KEY_PROPERTY, "password")
                .build();
        String encrypt = settingsEncryption.encrypt("Ada Lovelace", settings);
        Assertions.assertNotNull(encrypt);
    }

    @Test
    public void shoulDecrypt() {
        Settings settings = Settings.builder()
                .put(SymmetricSettingsEncryption.KEY_PROPERTY, "password")
                .build();
        String text = "Ada Lovelace";
        String encrypt = settingsEncryption.encrypt(text, settings);
        String decrypt = settingsEncryption.decrypt(encrypt, settings);
        Assertions.assertNotNull(encrypt);
        Assertions.assertNotNull(decrypt);
        assertEquals(text, decrypt);
    }

    //quando nao usar a chave
    //quando a chave for muito pequena

}