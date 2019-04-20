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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class AsymmetricSettingsEncryptionTest {


    private SettingsEncryption settingsEncryption;

    @BeforeEach
    public void setUp() {
        this.settingsEncryption = new AsymmetricSettingsEncryption();
    }

    @Test
    public void shouldReturnErrorWhenMissingParameter() {
        Settings settings = Settings.of(Collections.emptyMap());
        assertThrows(EncryptionException.class, () -> settingsEncryption.encrypt("Ada Lovelace", settings) );
        assertThrows(EncryptionException.class, () -> settingsEncryption.decrypt("Ada Lovelace", settings) );
    }

    @Test
    public void shouldReturnErrorWhenFileDoesNotExist() {
        Settings settings = Settings.builder()
                .put(AsymmetricSettingsEncryption.PRIVATE_PROPERTY, "wrong")
                .put(AsymmetricSettingsEncryption.PUBLIC_PROPERTY, "wrong")
                .build();

        assertThrows(EncryptionException.class, () -> settingsEncryption.encrypt("Ada Lovelace", settings) );
        assertThrows(EncryptionException.class, () -> settingsEncryption.decrypt("Ada Lovelace", settings) );
    }



}