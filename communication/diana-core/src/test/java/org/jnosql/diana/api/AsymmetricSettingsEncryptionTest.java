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

import org.jnosql.diana.api.AsymmetricSettingsEncryption;
import org.jnosql.diana.api.EncryptionException;
import org.jnosql.diana.api.Settings;
import org.jnosql.diana.api.SettingsEncryption;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AsymmetricSettingsEncryptionTest {


    private static final String PUBLIC_PATH = "target/pair/id_rsa.pub";
    private static final String PRIVATE_PATH = "target/pair/id_rsa.key";
    private static final int KEY_SIZE = 1024;
    private SettingsEncryption settingsEncryption;

    @BeforeEach
    public void setUp() {
        this.settingsEncryption = new AsymmetricSettingsEncryption();
    }

    @BeforeAll
    public static void beforeAll() throws NoSuchAlgorithmException, IOException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(KEY_SIZE);
        KeyPair pair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = pair.getPrivate();
        PublicKey publicKey = pair.getPublic();
        Path directory = Paths.get("target/pair/");
        if (!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
        Path publicPath = Paths.get(PUBLIC_PATH);
        Path privatePath = Paths.get(PRIVATE_PATH);

        Files.write(publicPath, publicKey.getEncoded());
        Files.write(privatePath, privateKey.getEncoded());
    }

    @AfterAll
    public static void afterAll() throws IOException {
        Path publicPath = Paths.get(PUBLIC_PATH);
        Path privatePath = Paths.get(PRIVATE_PATH);
        Files.delete(publicPath);
        Files.delete(privatePath);
    }

    @Test
    public void shouldReturnErrorWhenMissingParameter() {
        Settings settings = Settings.of(Collections.emptyMap());
        assertThrows(EncryptionException.class, () -> settingsEncryption.encrypt("Ada Lovelace", settings));
        assertThrows(EncryptionException.class, () -> settingsEncryption.decrypt("Ada Lovelace", settings));
    }

    @Test
    public void shouldReturnErrorWhenFileDoesNotExist() {
        Settings settings = Settings.builder()
                .put(AsymmetricSettingsEncryption.PRIVATE_PROPERTY, "wrong")
                .put(AsymmetricSettingsEncryption.PUBLIC_PROPERTY, "wrong")
                .build();

        assertThrows(EncryptionException.class, () -> settingsEncryption.encrypt("Ada Lovelace", settings));
        assertThrows(EncryptionException.class, () -> settingsEncryption.decrypt("Ada Lovelace", settings));
    }

    @Test
    public void shouldEncrypt() {
        Settings settings = Settings.builder()
                .put(AsymmetricSettingsEncryption.PRIVATE_PROPERTY, PRIVATE_PATH)
                .build();
        String encrypt = settingsEncryption.encrypt("Ada Lovelace", settings);
        assertNotNull(encrypt);
    }

    @Test
    public void shouldDecrypt() {
        Settings settings = Settings.builder()
                .put(AsymmetricSettingsEncryption.PUBLIC_PROPERTY, PUBLIC_PATH)
                .put(AsymmetricSettingsEncryption.PRIVATE_PROPERTY, PRIVATE_PATH)
                .build();
        String text = "Ada Lovelace";
        String encrypt = settingsEncryption.encrypt(text, settings);
        String decrypt = settingsEncryption.decrypt(encrypt, settings);
        assertNotNull(encrypt);
        assertNotNull(decrypt);
        assertEquals(text, decrypt);
    }

}