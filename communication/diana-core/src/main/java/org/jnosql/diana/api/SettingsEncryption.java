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

import java.util.Objects;

/**
 * This instance will encrypt and decrypt properties of {@link Settings}.
 * To set the encryption, put the value on the property: <b>jakarta.nosql.settings.encryption</b>.
 * The value is the {@link Class#getName()} from implementation and might define from {@link SettingsPriority},
 * in other words, the same priority from Eclipse MicroProfile Configuration and JSR 382.
 * Where the when sets the value: "symmetric" it will return {@link SymmetricSettingsEncryption}
 * and symmetric it will return {@link AsymmetricSettingsEncryption}.
 * To enable the property encryption put the value in the "ENC(value)";
 */
public interface SettingsEncryption {

    /**
     * The property to set the implementation encryption to use when it is required.
     */
    String ENCRYPTION_TYPE = "jakarta.nosql.settings.encryption";

    /**
     * Encrypts a property
     *
     * @param property encrypted property
     * @param settings Settings to read using the priority
     * @return the value encrypted
     * @throws NullPointerException when there is null parameters
     */
    String encrypt(String property, Settings settings);

    /**
     * Decrypts a property
     *
     * @param property decrypted property
     * @param settings Settings to read using the priority
     * @return the value decrypted
     * @throws NullPointerException when there is null parameters
     */
    String decrypt(String property, Settings settings);


    /**
     * Returns a {@link SettingsEncryption} instance reading the {@link SettingsEncryption#ENCRYPTION_TYPE}
     *
     * @return a {@link SettingsEncryption} instance
     */
    static SettingsEncryption get() {
        return SettingsEncryptionSupplier.INSTANCE.get();
    }

    /**
     * Returns a {@link SettingsEncryption} instance reading the {@link SettingsEncryption#ENCRYPTION_TYPE}
     *
     * @param settings the settings
     * @return a {@link SettingsEncryption} instance
     * @throws NullPointerException when settings is null
     */
    static SettingsEncryption get(Settings settings) {
        Objects.requireNonNull(settings, "settings is required");
        return SettingsEncryptionSupplier.INSTANCE.get(settings);
    }
}
